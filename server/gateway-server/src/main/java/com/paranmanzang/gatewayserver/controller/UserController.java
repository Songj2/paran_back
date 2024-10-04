package com.paranmanzang.gatewayserver.controller;

import com.paranmanzang.gatewayserver.Enum.Role;
import com.paranmanzang.gatewayserver.model.RegisterModel;
import com.paranmanzang.gatewayserver.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final Validator validator;

    public Mono<ServerResponse> insert(ServerRequest request) {
        return request.bodyToMono(RegisterModel.class)
                .doOnNext(userModel -> {
                    var errors = new BeanPropertyBindingResult(userModel, RegisterModel.class.getName());
                    validator.validate(userModel, errors);

                    if (errors.hasErrors()) {
                        throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
                    }
                })
                .flatMap(model -> userService.insert(model)
                        .flatMap(result -> ServerResponse.ok().bodyValue("회원가입 성공")))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("회원가입 실패: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("회원가입 처리 중 오류 발생"));

    }
    public Mono<ServerResponse> remove(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));
        log.info("nickname: " + nickname);
        return userService.remove(nickname)
                .flatMap(success -> {
                    if (success) {
                        // 삭제 성공 시 200 OK 응답 반환
                        return ServerResponse.ok().bodyValue("사용자가 성공적으로 삭제되었습니다.");
                    } else {
                        // 삭제 실패 시 404 Not Found 응답 반환
                        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue("사용자를 찾을 수 없습니다.");
                    }
                })
                .onErrorResume(e -> {
                    // 예외 발생 시 500 Internal Server Error 응답 반환
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("삭제 처리 중 오류 발생: " + e.getMessage());
                });
    }

    public Mono<ServerResponse> updatePassword(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));
        String newPassword = request.queryParam("newPassword")
                .orElseThrow(() -> new IllegalArgumentException("새 비밀번호가 필요합니다"));

        return userService.updatePassword(nickname, newPassword)
                .flatMap(success -> ServerResponse.ok().bodyValue("비밀번호가 성공적으로 업데이트되었습니다."))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("비밀번호 업데이트 실패: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("비밀번호 업데이트 중 오류 발생"));
    }


    public Mono<ServerResponse> updateDeclaration(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));
        return userService.updateDeclaration(nickname)
                .flatMap(success -> ServerResponse.ok().bodyValue("신고횟수가 성공적으로 업데이트되었습니다."))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("신고횟수 업데이트 실패: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("신고 횟수 업데이트 중 오류 발생"));
    }


    public Mono<ServerResponse> findAllByNickname(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("관리자 닉네임이 필요합니다"));

        return userService.findAllByNickname(nickname)
                .flatMap(users -> ServerResponse.ok().bodyValue(users))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.status(HttpStatus.FORBIDDEN).bodyValue("사용자 목록 조회 실패: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("사용자 목록 조회 중 오류 발생"));
    }

    public Mono<ServerResponse> findByNickname(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));

        return userService.findByNickname(nickname)
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.notFound().build())
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("사용자 상세 조회 중 오류 발생"));
    }
    public Mono<ServerResponse> checkNickname(ServerRequest request) {
        return request.bodyToMono(RegisterModel.class)
                .flatMap(registerModel -> userService.checkNickname(registerModel)
                        .flatMap(isAvailable -> {
                            if (isAvailable) {
                                return ServerResponse.ok().bodyValue("닉네임 사용 가능");
                            } else {
                                return ServerResponse.badRequest().bodyValue("닉네임이 이미 존재합니다.");
                            }
                        })
                )
                .onErrorResume(DataAccessException.class, e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue("닉네임 중복 확인 중 오류 발생: " + e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("닉네임 중복 확인 실패: " + e.getMessage()));
    }

    public Mono<ServerResponse> checkPassword(ServerRequest request) {
        return request.bodyToMono(RegisterModel.class)
                .flatMap(registerModel -> {
                    boolean isPasswordValid = userService.checkPassword(registerModel).block(); // 비밀번호 체크
                    if (isPasswordValid) {
                        return ServerResponse.ok().bodyValue("비밀번호가 일치합니다.");
                    } else {
                        return ServerResponse.badRequest().bodyValue("비밀번호가 일치하지 않습니다.");
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("비밀번호 체크 실패: " + e.getMessage()));
    }
    public Mono<ServerResponse> updateLogoutUserTime(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다."));

        log.info("로그아웃 요청: 닉네임 {}", nickname);

        return userService.updateLogoutTime(nickname)
                .flatMap(success -> {
                    if (success) {
                        return ServerResponse.ok().bodyValue("사용자의 로그아웃 시간이 업데이트되었습니다.");
                    } else {
                        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue("사용자를 찾을 수 없습니다.");
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("로그아웃 처리 중 오류 발생: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("로그아웃 처리 중 예기치 않은 오류 발생: " + e.getMessage()));
    }

    public Mono<ServerResponse> updateRole(ServerRequest request) {
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));
        Role newRole = Role.valueOf(request.queryParam("newRole")
                .orElseThrow(() -> new IllegalArgumentException("새로운 권한이 필요합니다")));

        return userService.updateRole(nickname, newRole)
                .flatMap(success -> {
                    if (success) {
                        return ServerResponse.ok().bodyValue("사용자의 권한이 성공적으로 업데이트되었습니다.");
                    } else {
                        return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue("사용자를 찾을 수 없습니다.");
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("권한 업데이트 실패: " + e.getMessage()))
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("권한 업데이트 중 오류 발생"));
    }

    public Mono<ServerResponse> checkRole(ServerRequest request){
        String nickname = request.queryParam("nickname")
                .orElseThrow(() -> new IllegalArgumentException("닉네임이 필요합니다"));
        return userService.checkRole(nickname)
                .flatMap(role -> ServerResponse.ok().bodyValue("사용자의 권한: " + role))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest().bodyValue("권한 조회 실패: " + e.getMessage())) // 잘못된 요청(닉네임이 잘못되거나 사용자 없음)
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("권한 조회 중 오류 발생: " + e.getMessage())); // 그 외 오류
    }
}
