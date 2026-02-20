package miniProject.todo_list.user.Service;

import lombok.RequiredArgsConstructor;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Dto.UserUpdateDto;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor //final 붙은 필드만 모아서 생성자 자동 생성
public class JpaUserServiceImpl implements UserService{

    private final JpaUserRepository jpaUserRepository;


/*    // @RequiredArgsConstructor 붙이면 생략 가능
    public JpaUserServiceImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }
*/


    // 유저 조회 검사 로직
    private User getUserOrThrow(Long userId) {
        System.out.println("userId = " + userId);
        return jpaUserRepository.findById(userId).orElseThrow(() ->
                new IllegalStateException("유저 조회 실패! 해당 Id의 유저가 존재하지 않습니다."));
    }

    // User Entity List -> User Dto List 변경 로직
    private List<UserResponseDto> toUserResponseDto(List<User> userList) {
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for(User user : userList) {
            userResponseDtos.add(UserResponseDto.fromEntity(user));
        }
        if(userList.size()==0) {
            throw new IllegalStateException("해당하는 유저 목록이 없습니다.");
        }
        return userResponseDtos;
    }


    // 유저 생성
    @Override
    public UserResponseDto joinUser(UserJoinDto dto) {
        System.out.println("전달받은 이메일 = [" + dto.getEmail()+"]");

        // 전달 받은 값에 이메일이 있는지 확인
        if(dto.getEmail()==null) {
            throw new IllegalStateException("이메일을 입력하세요.");
        }
        User findUser = jpaUserRepository.findByEmail(dto.getEmail());

        // 받은 이메일이 중복 이메일인지 확인
        if(findUser!=null) {
            throw new IllegalStateException("유저 생성 실패! 이미 존재하는 이메일입니다.");
        }
        User user = dto.toEntity();
        User savedUser = jpaUserRepository.save(user);

        return UserResponseDto.fromEntity(savedUser);
    }

    // 유저 탈퇴 (삭제)
    @Override
    public Long quitUser(Long requestId, Long targetId) {
        // 권한 인증 로직
        if(!requestId.equals(targetId)) {
            throw new IllegalStateException("유저 삭제 실패! 요청된 Id와 삭제할 Id가 같지 않습니다.");
        }

        User findUser = getUserOrThrow(targetId);
        jpaUserRepository.delete(findUser);
        return targetId;
    }

    @Override
    public UserResponseDto findUserById(Long userId) {
        User findUser = getUserOrThrow(userId);
        return UserResponseDto.fromEntity(findUser);
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = jpaUserRepository.findAll();
        return toUserResponseDto(userList);
    }

    // 유저 정보 변경 (유저 이메일, 이름 변경)
    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto dto) {
        User findUser = getUserOrThrow(userId);

        if(dto.getEmail()!=null) {
            findUser.setEmail(dto.getEmail());
        }
        if(dto.getUserName()!=null) {
            findUser.setUserName(dto.getUserName());
        }

        return UserResponseDto.fromEntity(findUser);
    }

    // 이메일로 특정 유저 찾기
    @Override
    public UserResponseDto findUserByEmail(String email) {
        User findUser = jpaUserRepository.findByEmail(email);
        if(findUser == null) {
            throw new IllegalStateException("유저 조회 실패! 해당 이메일의 유저가 없습니다.");
        }
        return UserResponseDto.fromEntity(findUser);
    }

    // 이름으로 특정 유저 찾기
    @Override
    public List<UserResponseDto> findUserByUserName(String userName) {
        List<User> userList= jpaUserRepository.findByUserName(userName);
        return toUserResponseDto(userList);
    }
}
