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
        int rand1 = (int)(Math.random() * 6) + 1;
        int rand2 = (int)(Math.random() * 6) + 1;
        int rand3 = (int)(Math.random() * 6) + 1;
        int count = 0;
        if(n == rand1) {
            count++;
        }
        if(n == rand2) {
            count++;
        }
        if (n == rand3) {
            count++;
        }
        String result = "You guessed correctly " + count + " times!";
        model.addAttribute("rand1", "dice roll 1: " + rand1);
        model.addAttribute("rand2", "dice roll 2: " + rand2);
        model.addAttribute("rand3", "dice roll 3: " + rand3);
        model.addAttribute("guess", "Your guess: " + n);
        model.addAttribute("result", result);
        return "roll-dice";
    }


}
