package com.eShope.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="settings")
public class Setting {
    @Id
    @Column(name = "`key`",nullable = false,length = 128)
    private String key;

    @Column(nullable = false,length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length =45,nullable = false)
    private SettingCategory category;
}
