package com.fubao.project.domain.entity;
import com.fubao.project.global.common.constant.MemberRole;
import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.constant.State;
import com.fubao.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fubao_love")
public class FubaoLove {
    @Id
    int id;
    @Column(name = "love")
    long love;

    public void updateLove(Long love) {
        this.love = love;
    }
}
