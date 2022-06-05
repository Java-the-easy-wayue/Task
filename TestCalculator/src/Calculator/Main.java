package Calculator;

import java.io.*;
import java.util.*;


class CalcException extends Exception{

    public CalcException(){
        System.out.println("Не правильно задано выражение");
    }

    public CalcException(String message){
        this();
        System.out.println(message);
    }
}

class Test{

    private int[] arab = new int[]{100,90,50,40,10,9,5,4,1};
    private String[] roman = new String[]{"C","XC","L","XL","X","IX","V","IV","I"};

    private Map<String,Integer> romeAndArab = new TreeMap<>();

    public Test(){
        romeAndArab.put("I",1);
        romeAndArab.put("II",2);
        romeAndArab.put("III",3);
        romeAndArab.put("IV",4);
        romeAndArab.put("V",5);
        romeAndArab.put("VI",6);
        romeAndArab.put("VII",7);
        romeAndArab.put("VIII",8);
        romeAndArab.put("IX",9);
        romeAndArab.put("X",10);
    }

    //---проверка, что это арабское число
    public boolean isArabNumber(String arabic){
        try {
            Integer.parseInt(arabic);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }


    //---проверка, что это римское число (I..X)
    public boolean isRoman(String rome){
        return romeAndArab.containsKey(rome);
    }

    public Integer romeToArabConvert(String str){
        if (romeAndArab.containsKey(str)){
            return romeAndArab.get(str);
        }
        return null;
    }

    public String arabToRomeConvert(int digit){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arab.length; i++){
            while ((digit - arab[i])>=0){
                digit -= arab[i];
                sb.append(roman[i]);
            }
        }
        return sb.toString();
    }

    //---проверка оператора, должно быть: +-*/
    public boolean checkOperator(String opera){
        return "+".equals(opera) || "-".equals(opera) || "*".equals(opera) || "/".equals(opera);
    }

}

class Calculator{
    private int numberOne;
    private int numberTwo;
    private String operator;


    private int Math(int a,String opera, int b){
        int result;
        switch (opera){
            case "+": result = a + b; break;
            case "-": result = a - b; break;
            case "*": result = a * b; break;
            case "/": result = a / b; break;
            default: throw new IllegalArgumentException();
        }
        return result;
    }


    public String calc(String input) throws CalcException {
        String exp = input.toUpperCase();
        boolean isRomanNum;

        Test test = new Test();

        List<String> list = Arrays.asList(exp.split(" "));

        if (list.size()!=3){
            throw new CalcException("Ошибка. Выражение должно иметь вид: \"Число1 Операция Число2\", разделенные пробелом...");
        }

        //--- проверка оператора, должен быть: + - * /
        if (test.checkOperator(list.get(1))){
            operator = list.get(1);
        }else {
            throw new CalcException("ОШИБКА. Оператор '" + list.get(1) + "' не корректен, должен быть: + - * / ");
        }

        //---проверка чисел, должны быть оба арабские или оба римские
        if (test.isArabNumber(list.get(0)) && test.isArabNumber(list.get(2))){      //---проверяем, что оба числа арабские
            numberOne = Integer.parseInt(list.get(0));
            numberTwo = Integer.parseInt(list.get(2));
            isRomanNum = false;
        }else if (test.isRoman(list.get(0)) && test.isRoman(list.get(2))){          //---проверяем, что оба числа римские
            numberOne = test.romeToArabConvert(list.get(0));
            numberTwo = test.romeToArabConvert(list.get(2));
            isRomanNum = true;
        }else {
            throw new CalcException("Ошибка. Числа должны быть оба арабские или оба римские");
        }


        //---проверка чисел, должны быть от 1 до 10 включительно
        if (!(numberOne>=1 && numberOne<=10)){
            throw new CalcException("ОШИБКА. Число #1 должно быть от 1 до 10 или от I до X включительно");
        }

        if (!(numberTwo>=1 && numberTwo<=10)){
            throw new CalcException("ОШИБКА. Число #2 должно быть от 1 до 10 или от I до X включительно");
        }

        //--- получаем результат
        int result = Math(numberOne,operator,numberTwo);

        if (isRomanNum){
            if (result<0){
                throw new CalcException("Результать не можеть быть отрицательным ["+result+"]");
            }
            return test.arabToRomeConvert(result);
        }

        return String.valueOf(result);
    }

}

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Калькулятор для выражений вида: \"Число1 Операция Число2\", через пробел. Допускаются числа от 1 до 10 или от I до X включительно. Операции: + - * /");
            System.out.print("Введите выражение: ");
           Scanner scan = new Scanner(System.in);
          String input = scan.nextLine();

          Calculator calculator = new Calculator();
          String result = calculator.calc(input);
          System.out.println("Результать: " + result);

        } catch (CalcException e) {

        }

    }
}
