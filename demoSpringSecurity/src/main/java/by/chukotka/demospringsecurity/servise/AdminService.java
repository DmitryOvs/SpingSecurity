package by.chukotka.demospringsecurity.servise;


import by.chukotka.demospringsecurity.Model.Admin;
import by.chukotka.demospringsecurity.Model.Role;
import by.chukotka.demospringsecurity.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

     private final AdminRepository repository;

        /**
         * Сохранение пользователя
         *
         * @return сохраненный пользователь
         */
        public Admin save(Admin admin) {
            return repository.save(admin);
        }


        /**
         * Создание пользователя
         *
         * @return созданный пользователь
         */
        public Admin create(Admin admin) {
            if (repository.existsByUsername(admin.getUsername())) {
                // Заменить на свои исключения
                throw new RuntimeException("Пользователь с таким именем уже существует");
            }
            if (repository.existsByEmail(admin.getEmail())) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            return save(admin);
        }

        /**
         * Получение пользователя по имени пользователя
         *
         * @return пользователь
         */
        public Admin getByUsername(String username) {
            return repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        }

        /**
         * Получение пользователя по имени пользователя
         * <p>
         * Нужен для Spring Security
         *
         * @return пользователь
         */
        public UserDetailsService userDetailsService()
        {return this::getByUsername;
        }

        /**
         * Получение текущего пользователя
         *
         * @return текущий пользователь
         */
        public Admin getCurrentUser() {
            // Получение имени пользователя из контекста Spring Security
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            return getByUsername(username);
        }


        /**
         * Выдача прав администратора текущему пользователю
         * <p>
         * Нужен для демонстрации
         */
        @Deprecated
        public void getAdmin() {
            var user = getCurrentUser();
            user.setRole(Role.ROLE_ADMIN);
            save(user);
        }
    }
