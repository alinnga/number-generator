package com.springboot.numbersgai.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/number")
public class NumbersController {
	
	final List<Character> letter = Arrays.asList(
			'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х');
	
	Set<String> previousNumbers = new HashSet<>();
	String previousNumber;
	Random rand = new Random();
	final String constant = " 116 RUS";
	

	@GetMapping("/random")
	public StringBuilder getRandom() {
		int[] randomArr = generateRandomArray();
		StringBuilder number = toStringBuilder(randomArr);

		while(previousNumbers.contains(number.toString())) {
			randomArr = generateRandomArray();
			number = toStringBuilder(randomArr);
		}
		previousNumbers.add(number.toString());
		previousNumber = number.toString();
		number.append(constant);

		return number;
	}
	
	@GetMapping("/next")
	public String getNext() {
		String nextNumber = generateNextNumber(previousNumber);

		while(previousNumbers.contains(nextNumber)) {
			nextNumber = generateNextNumber(nextNumber);
		}
		
		previousNumbers.add(nextNumber);
		
		return nextNumber+constant;
	}
	
	@GetMapping("/numbers")
	public Set<String> getAllNumbers() {
		return previousNumbers;
	}
	
	// Из массива чисел составляется номер в формате A111AA
	private StringBuilder toStringBuilder(int[] randomArr) {
		StringBuilder str = new StringBuilder().append(letter.get(randomArr[0])).append(randomArr[3])
				.append(randomArr[4]).append(randomArr[5]).append(letter.get(randomArr[1])).append(letter.get(randomArr[2]));
		return str;
	}
	
	//Создается массив из 6 рандомных целых чисел. 
	// 3 числа  в диапозоне [0-9] для цифр в номере
	// и 3 числа в диапозоне [0-11] - индексы для букв
	private int[] generateRandomArray() {
		int[] randomNum = new int[6];
		
		for(int i=0; i<6; i++) {
			if(i<3) {
				randomNum[i] = rand.nextInt(12);
			}
			else {
				randomNum[i] = rand.nextInt(10);
			}
		}

		return randomNum;
	}
	
	// На вход подается 3-х буквенное слово 
	// и возвращается следующее по порядку слово АНТ -> AНУ
	private StringBuilder nextWord(StringBuilder word) {
		if(word.charAt(2)!=letter.get(letter.size()-1)) {
			String newLetter = letter.get(letter.indexOf(word.charAt(2))+1)+"";
			word.replace(2, 3, newLetter);
		}
		else {
			if(word.charAt(1)!=letter.get(letter.size()-1)) {
				String newLetter = letter.get(letter.indexOf(word.charAt(1))+1)+"";
				String startLetter = letter.get(0)+"";
				
				word.replace(1, 2, newLetter);
				word.replace(2, 3, startLetter);
			}
			else {
				if(word.charAt(0)!=letter.get(letter.size()-1)) {
					String newLetter = letter.get(letter.indexOf(word.charAt(0))+1)+"";
					String startLetter = letter.get(0)+"";
					
					word.replace(0, 1, newLetter);
					word.replace(1, 2, startLetter);
					word.replace(2, 3, startLetter);
				}
			}
		}
		return word;
	}
	
	//Возвращается следующий номер A112BC -> A113BC
	private String generateNextNumber(String prevNum) {
		StringBuilder nextNum = new StringBuilder(prevNum);
		int digits = Integer.parseInt(nextNum.substring(1, 4));

		if(digits>=999) {
			StringBuilder letterPart = new StringBuilder().append(nextNum
					.substring(0,1))
					.append(nextNum.substring(4,5))
					.append(nextNum.substring(5,6));
			
			StringBuilder nextWord = nextWord(letterPart);
			nextNum.replace(0, 1, nextWord.substring(0, 1));
			nextNum.replace(4, 5, nextWord.substring(1, 2));
			nextNum.replace(5, 6, nextWord.substring(2, 3));
		}
		else {
			digits++;
			nextNum.replace(1, 4, digits+"");
		}
		return nextNum.toString();
	}
}

