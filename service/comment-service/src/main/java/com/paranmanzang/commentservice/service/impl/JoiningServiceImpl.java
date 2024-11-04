package com.paranmanzang.commentservice.service.impl;

import com.paranmanzang.commentservice.model.domain.ErrorField;
import com.paranmanzang.commentservice.model.domain.JoiningModel;
import com.paranmanzang.commentservice.model.entity.Joining;
import com.paranmanzang.commentservice.model.repository.GroupRepository;
import com.paranmanzang.commentservice.model.repository.JoiningRepository;
import com.paranmanzang.commentservice.service.JoiningService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoiningServiceImpl implements JoiningService {
    private final JoiningRepository joiningRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public Object addMember(JoiningModel joiningModel) {
        return joiningRepository.findJoiningByGroupIdAndNickname(joiningModel.getGroupId(), joiningModel.getNickname())
                .map(joining -> (Object) new ErrorField(joiningModel.getNickname(), "이미 가입되어있는 멤버입니다."))
                .orElseGet(() -> groupRepository.findById(joiningModel.getGroupId())
                        .map(group -> (Object) JoiningModel.fromEntity(joiningRepository.save(Joining.builder()
                                .nickname(joiningModel.getNickname())
                                .group(groupRepository.findById(joiningModel.getGroupId()).get())
                                        .responseAt(LocalDate.now())
                                .build())))
                        .orElseGet(() -> (Object) new ErrorField(joiningModel.getNickname(), "그룹을 찾을 수 없습니다.")));
    }

    public Object enableMember(Long groupId, String nickname) {
        return joiningRepository.findByGroupIdAndNickname(groupId, nickname)
                .map(joiningToEnable -> {
                    if (!joiningToEnable.isEnabled()) {
                        joiningToEnable.setEnabled(true);
                        return JoiningModel.fromEntity(joiningRepository.save(joiningToEnable));
                    } else {
                        return (Object) new ErrorField(nickname, "이미 승인된 멤버입니다.");
                    }
                })
                .orElseGet(() -> new ErrorField(nickname, "가입정보가 없습니다."));
    }


    public Object disableMember(Long groupId, String nickname) {
        return joiningRepository.findByGroupIdAndNickname(groupId, nickname)
                .map(joiningToEnable -> {
                    if (joiningToEnable.isEnabled()) {
                        joiningToEnable.setEnabled(false);
                        return JoiningModel.fromEntity(joiningRepository.save(joiningToEnable));
                    } else {
                        return (Object) new ErrorField(nickname, "미승인 멤버입니다.");
                    }
                })
                .orElseGet(() -> new ErrorField(nickname, "가입정보가 없습니다."));
    }


    @Override
    public Object getUserListById(Long groupId) {
        return joiningRepository.findByGroupId(groupId).stream()
                .map(JoiningModel::fromEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public Object deleteUser(String nickname, Long groupId) {
        try{
            System.out.println("target: "+ nickname+", "+ groupId);
            System.out.println("result: "+ joiningRepository.findByNicknameAndGroupId(nickname,groupId));
            joiningRepository.delete(joiningRepository.findByNicknameAndGroupId(nickname,groupId));
            return true;
        }catch (Error e){
            return false;
        }
    }
}
