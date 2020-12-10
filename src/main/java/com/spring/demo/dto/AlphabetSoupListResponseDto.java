package com.spring.demo.dto;

import java.util.List;

import com.spring.demo.models.AlphabetSoupModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlphabetSoupListResponseDto {

	private List<AlphabetSoupModel> list;
}
