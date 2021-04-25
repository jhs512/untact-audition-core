package com.audictionary.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recruit extends EntityDto {
	
	private int id;
	private String regDate;
	private String updateDate;
	private String delDate;
	private int delStatus;
	private int memberId;
	private int boardId;
	private String title;
	private String body;
	private String roleType;
	private String pay;
	private String location;
	private String period;
	private String deadline;
	private String gender;
	private String age;
	private String script;
	private String videoTime;
	private String etc;
	
	private int extra__application__count;
	
	private String extra__aw_media;
	private String extra__aw_title;
	private String extra__aw_subtitle;
	private String extra__aw_genre;
	private String extra__aw_director;
	private String extra__aw_writer;
	private String extra__aw_corp;
	private String extra__aw_producer;
	private String extra__aw_castingManager;
	private String extra__aw_story;
	private String extra__aw_etc;

	private String extra__ar_realName;
	private String extra__ar_name;
	private String extra__ar_age;
	private String extra__ar_job;
	private String extra__ar_gender;
	private String extra__ar_scriptStatus;
	private String extra__ar_scenesCount;
	private String extra__ar_shootingsCount;
	private String extra__ar_character;
	private String extra__ar_etc;
	
	
	
}
