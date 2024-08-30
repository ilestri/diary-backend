package org.diary.diarybackend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.diary.diarybackend.repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class PartnerRequestService {
    private UsersRepository usersRepository;
    //private PartnerRepository partnerRepository;
    @Transactional
    
    public void addPartner(Long userid, Long partnerid) {
       //사용자가 존재하는지 확인
        // 이미 파트너로 등록되어있는지 확인
        //친구 추가
    }


}
