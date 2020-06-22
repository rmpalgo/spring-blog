package com.codeup.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RollDice {

    @GetMapping("/roll-dice")
    public String showForm() {
        return "roll-dice";
    }

    @GetMapping("/roll-dice/{n}")
    public String result(@PathVariable int n, Model model) {
        int rand = (int)(Math.random() * 6) + 1;
        String result;
        if(n == rand) {
            result = "You guessed right!";
        } else {
            result = "You guessed wrong!";
        }
        model.addAttribute("rand", "dice roll: " + rand);
        model.addAttribute("guess", "Your guess: " + n);
        model.addAttribute("result", result);
        return "roll-dice";
    }


}
