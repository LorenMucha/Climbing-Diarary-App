package com.main.climbingdiary.models;

import java.util.Arrays;

public class Levels {
   public static String[] getLevels(){
       return new String[]{"6a", "6a+", "6b", "6b+", "6c", "6c+", "7a", "7a+", "7b", "7b+", "7c", "7c+", "8a", "8a+", "8b", "8b+", "8c", "8c+", "9a"};
   };

   public static int getLevelRating(String level){
       return (Arrays.asList(getLevels()).indexOf(level)*5);
   }
}
