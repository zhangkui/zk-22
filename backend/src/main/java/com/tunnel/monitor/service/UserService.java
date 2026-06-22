package com.tunnel.monitor.service;

import com.tunnel.monitor.dto.LoginRequest;
import com.tunnel.monitor.dto.LoginResponse;
import com.tunnel.monitor.dto.PageRequest;
import com.tunnel.monitor.dto.PageResult;
import com.tunnel.monitor.entity.User;
import com.tunnel.monitor.repository.UserRepository;
import com.tunnel.monitor.util.BusinessException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    UserRepository userRepository;

    @Inject
    MockDataService mockDataService;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.expire.hours", defaultValue = "24")
    long expireHours;

    public Uni<LoginResponse> login(LoginRequest request) {
        User user = mockDataService.getUserByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (!user.getEnabled()) {
            throw new BusinessException(403, "用户已被禁用");
        }

        if (!BcryptUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String token = Jwt.issuer(issuer)
                .subject(user.getUsername())
                .upn(user.getUsername())
                .groups(new HashSet<>(Arrays.asList(user.getRole())))
                .claim("userId", user.getId())
                .claim("realName", user.getRealName())
                .claim("role", user.getRole())
                .expiresIn(expireHours * 3600)
                .sign();

        user.setLastLoginTime(LocalDateTime.now());

        return Uni.createFrom().item(new LoginResponse(
                token,
                "Bearer",
                expireHours * 3600,
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getRole()
        ));
    }

    public Uni<PageResult<User>> list(PageRequest pageRequest) {
        List<User> users = mockDataService.getMockUsers();
        int total = users.size();
        int start = pageRequest.getPageIndex() * pageRequest.getSize();
        int end = Math.min(start + pageRequest.getSize(), total);
        List<User> records = users.subList(start, end);

        return Uni.createFrom().item(PageResult.of(records, total, pageRequest.getPage(), pageRequest.getSize()));
    }

    public Uni<User> getById(Long id) {
        User user = mockDataService.getUser(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return Uni.createFrom().item(user);
    }

    public Uni<User> create(User user) {
        user.setId(null);
        user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.persist(user);
    }

    public Uni<User> update(Long id, User user) {
        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessException(404, "用户不存在"))
                .onItem().transform(existing -> {
                    existing.setUsername(user.getUsername() != null ? user.getUsername() : existing.getUsername());
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existing.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
                    }
                    existing.setRealName(user.getRealName() != null ? user.getRealName() : existing.getRealName());
                    existing.setPhone(user.getPhone() != null ? user.getPhone() : existing.getPhone());
                    existing.setEmail(user.getEmail() != null ? user.getEmail() : existing.getEmail());
                    existing.setRole(user.getRole() != null ? user.getRole() : existing.getRole());
                    existing.setAvatar(user.getAvatar() != null ? user.getAvatar() : existing.getAvatar());
                    existing.setEnabled(user.getEnabled() != null ? user.getEnabled() : existing.getEnabled());
                    existing.setUpdateTime(LocalDateTime.now());
                    existing.setRemark(user.getRemark() != null ? user.getRemark() : existing.getRemark());
                    return existing;
                });
    }

    public Uni<Boolean> delete(Long id) {
        return userRepository.deleteById(id);
    }
}
