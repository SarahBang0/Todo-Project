package miniProject.todo_list.user.Service;

import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class JpaUserServiceImpl implements UserService{

    private final JpaUserRepository jpaUserRepository;
    private final JpaTodoRepository jpaTodoRepository;

    public JpaUserServiceImpl(JpaUserRepository jpaUserRepository, JpaTodoRepository jpaTodoRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    public UserResponseDto joinUser(UserJoinDto dto) {
        System.out.println("전달받은 이메일 = [" + dto.getEmail()+"]");

        if(dto.getEmail()==null) {
            throw new IllegalStateException("이메일을 입력하세요.");
        }
        User findUser = jpaUserRepository.findByEmail(dto.getEmail());
        if(findUser!=null) {
            throw new IllegalStateException("유저 생성 실패! 이미 존재하는 이메일입니다.");
        }
        User user = dto.toEntity();
        User savedUser = jpaUserRepository.save(user);

        return UserResponseDto.fromEntity(savedUser);
    }

    @Override
    public Long quitUser(Long requestId, Long targetId) {
        // 권한 인증 로직
        if(!requestId.equals(targetId)) {
            throw new IllegalStateException("유저 삭제 실패! 요청된 Id와 삭제할 Id가 같지 않습니다.");
        }

        User findUser = jpaUserRepository.findById(targetId)
                .orElseThrow(()-> new IllegalStateException("유저 삭제 실패! 해당 Id의 유저가 없습니다."));
        jpaUserRepository.delete(findUser);
        return targetId;
    }

    @Override
    public UserResponseDto findUserById(Long id) {
        User findUser = jpaUserRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("조회 실패! 해당 Id의 유저가 없습니다."));
        return UserResponseDto.fromEntity(findUser);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = jpaUserRepository.findAll();
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for(User user : userList) {
            userResponseDtos.add(UserResponseDto.fromEntity(user));
        }
        return userResponseDtos;
    }
}
