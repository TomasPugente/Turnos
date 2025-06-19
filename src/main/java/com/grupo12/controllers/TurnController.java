package com.grupo12.controllers;

import com.grupo12.converters.TurnConverter;
import com.grupo12.entities.Client;
import com.grupo12.entities.TurnStatus;
import com.grupo12.exceptions.TurnNotFoundException;
import com.grupo12.helpers.ViewRouteHelper;
import com.grupo12.entities.Turn;
import com.grupo12.models.ClientDTO;
import com.grupo12.models.TurnDTO;
import com.grupo12.models.TurnUpdateDTO;
import com.grupo12.services.implementation.ClientService;
import com.grupo12.services.implementation.TurnService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/turns")
public class TurnController {

    private final TurnService turnService;
    private final TurnConverter turnConverter;

    public TurnController(TurnService turnService, TurnConverter turnConverter) {
        this.turnService = turnService;
        this.turnConverter = turnConverter;
    }


    @GetMapping
    public String listTurns(Model model) {
        List<TurnDTO> turns = turnService.getAll().stream()
                .map(turnConverter::entityToDTO)
                .collect(Collectors.toList());
        model.addAttribute("turns", turns);
        return "turn/list"; // este HTML
    }

    // Mostrar el formulario para crear o editar
    @GetMapping("/form/{id}")
    public String showForm(@PathVariable(required = false) Integer id, Model model) {
        if (id != null) {
            Optional<Turn> turn = turnService.getById(id);
            if (turn.isPresent()) {
                model.addAttribute("turnDTO", turnConverter.entityToDTO(turn.get())); // para editar
            } else {
                return "redirect:/clients";
            }

        } else {
            model.addAttribute("turnDTO", new TurnDTO()); // para nuevo
        }
        model.addAttribute("statusOptions", TurnStatus.values());

        return "turn/form"; // templates/client/form.html
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("turnDTO") TurnDTO turnDTO, BindingResult bindingResult,
                             Model model) {

        turnService.insertOrUpdate(turnDTO);
        return "redirect:/turns";
    }

    // Muestra el formulario para editar
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Turn turn = turnService.findById(id);
            model.addAttribute("turn", turn);
            return "editTurn";
        } catch (TurnNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home/index";
        }
    }

    // Procesa el formulario
    @PostMapping("/update/{id}")
    public String updateTurn(@PathVariable Long id,
                             @Valid @ModelAttribute("turn") TurnUpdateDTO turnUpdateDTO,
                             RedirectAttributes redirectAttributes) {
        try {
            turnService.updateTurn(id, turnUpdateDTO);
            redirectAttributes.addFlashAttribute("success", "Turno actualizado correctamente.");
        } catch (TurnNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Turno no encontrado.");
        }

        return "redirect:/home/index";
    }
}
