package org.userservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.userservice.exception.UserNotFoundException;
import org.userservice.mapper.UserMapper;
import org.userservice.model.UserDtoIn;
import org.userservice.model.UserDtoOut;
import org.userservice.model.UserNotificationEvent;
import org.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String KAFKA_TOPIC = "user-events";

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserServiceImpl(UserRepository userRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public UserDtoOut createUser(UserDtoIn userDto) {
        var userEntity = UserMapper.toEntity(userDto);
        var createdUser = userRepository.save(userEntity);

        var event = new UserNotificationEvent(userDto.email(), "CREATE");
        kafkaTemplate.send(KAFKA_TOPIC, event);
        logger.info("Сообщение отправлено в Kafka: {} (CREATE)", userDto.email());

        logger.info("Пользователь создан: {}", createdUser.getId());
        return UserMapper.toDto(createdUser);
    }

    @Override
    public UserDtoOut getById(Long id) {
        var existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return UserMapper.toDto(existingUser.get());
    }

    @Override
    public List<UserDtoOut> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDtoOut updateUser(Long id, UserDtoIn userDto) {
        var existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        existingUser.get().setName(userDto.name());
        existingUser.get().setEmail(userDto.email());
        existingUser.get().setAge(userDto.age());

        var updated = userRepository.save(existingUser.get());
        logger.info("Пользователь обновлён: {}", id);
        return UserMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        var existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        var email = existingUser.get().getEmail();

        var event = new UserNotificationEvent(email, "DELETE");
        kafkaTemplate.send(KAFKA_TOPIC, event);
        logger.info("Сообщение отправлено в Kafka: {} (DELETE)", email);

        userRepository.deleteById(id);
        logger.info("Пользователь удалён: {}", id);
    }
}
