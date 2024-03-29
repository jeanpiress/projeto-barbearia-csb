package com.jeanpiress.ProjetoBarbaria.api.exceptionHandlers;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Problem {

	public String title;
	public String detail;
	public List<Field> fields;
	
	
	@Getter
	@Builder
	public static class Field{
		
		private String name;
		private String userMessage;
	}
}


