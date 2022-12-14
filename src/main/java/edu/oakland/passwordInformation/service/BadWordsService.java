package edu.oakland.passwordInformation.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class BadWordsService {

  private static Map<String, Boolean> words;

  public BadWordsService() {
    words = new HashMap<String, Boolean>();
    words.put("anal", true);
    words.put("anus", true);
    words.put("ass", true);
    words.put("ballsack", true);
    words.put("balls", true);
    words.put("bastard", true);
    words.put("bitch", true);
    words.put("biatch", true);
    words.put("bloody", true);
    words.put("blowjob", true);
    words.put("bollock", true);
    words.put("bollok", true);
    words.put("boner", true);
    words.put("boob", true);
    words.put("bugger", true);
    words.put("bum", true);
    words.put("butt", true);
    words.put("buttplug", true);
    words.put("clitoris", true);
    words.put("cock", true);
    words.put("coon", true);
    words.put("crap", true);
    words.put("cunt", true);
    words.put("damn", true);
    words.put("dick", true);
    words.put("dildo", true);
    words.put("dyke", true);
    words.put("fag", true);
    words.put("feck", true);
    words.put("fellate", true);
    words.put("fellatio", true);
    words.put("felching", true);
    words.put("fuck", true);
    words.put("fudgepacker", true);
    words.put("flange", true);
    words.put("goddamn", true);
    words.put("hell", true);
    words.put("homo", true);
    words.put("jerk", true);
    words.put("jizz", true);
    words.put("knobend", true);
    words.put("labia", true);
    words.put("lmao", true);
    words.put("lmfao", true);
    words.put("muff", true);
    words.put("nigger", true);
    words.put("nigga", true);
    words.put("omg", true);
    words.put("penis", true);
    words.put("piss", true);
    words.put("poop", true);
    words.put("prick", true);
    words.put("pube", true);
    words.put("pussy", true);
    words.put("queer", true);
    words.put("scrotum", true);
    words.put("sex", true);
    words.put("shit", true);
    words.put("slut", true);
    words.put("smegma", true);
    words.put("spunk", true);
    words.put("tosser", true);
    words.put("turd", true);
    words.put("twat", true);
    words.put("vagina", true);
    words.put("wank", true);
    words.put("whore", true);
    words.put("wtf", true);
  }

  public boolean isOnList(String word) {
    if (words.get(word.toLowerCase()) == null) {
      return false;
    }
    return true;
  }
}
