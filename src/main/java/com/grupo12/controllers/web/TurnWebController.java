package com.grupo12.controllers.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.grupo12.models.TurnDTO;
import com.grupo12.services.ITurnService;

@Controller
@RequestMapping("/web/turns")
public class TurnWebController {

    @Autowired
    private ITurnService turnService;

    @GetMapping("/generados")
    public String mostrarTurnosGenerados(
            @RequestParam int employeeId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam int durationMinutes,
            Model model) {

        List<TurnDTO> turnos = turnService.enableMultipleTurns(employeeId, serviceId, startDate, endDate, durationMinutes);
        model.addAttribute("turnos", turnos);
        return "listTurns"; // nombre del archivo lista_turnos.html
    }


    @GetMapping("/turnos/seleccionar")
    public String mostrarTurnosDisponibles(
            @RequestParam int employeeId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam int durationMinutes,
            Model model) {

        List<TurnDTO> turnos = turnService.enableMultipleTurns(employeeId, serviceId, startDate, endDate, durationMinutes);
        model.addAttribute("turnos", turnos);
        return "turnsView"; // Esto se refiere a un archivo `turnos_disponibles.html` en `src/main/resources/templates`
    }
    
    @GetMapping("/form")
    public String mostrarFormulario() {
        return "generateTurn"; // sin la extensión .html
    }
    
    @GetMapping("/reminders")
    public String mostrarTurnosParaRecordatorio(Model model) {
        // Por ejemplo, los próximos 24h
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        List<TurnDTO> turnos = turnService.getUpComingTurnsForReminders(now, tomorrow);
        model.addAttribute("turnos", turnos);
        return "reminders";
    }
    /*
    @PostMapping("/send-reminders")
    public String enviarRecordatoriosManual(RedirectAttributes redirectAttributes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        turnService.getUpComingTurnsForReminders(now, tomorrow); // Ya incluye el envío de mails
        redirectAttributes.addFlashAttribute("mensaje", "Recordatorios enviados correctamente.");
        return "redirect:/web/turns/reminders";
    }
    @PostMapping("/send-reminders")
    public String enviarRecordatorios(@RequestParam("selectedTurnIds") List<Integer> ids, Model model) {
        List<TurnDTO> recordatorios = turnService.sendRemindersTo(ids); // nuevo método
        model.addAttribute("enviados", recordatorios);
        return "remindersResult"; // podés hacer una vista de éxito
    }*/
    @PostMapping("/send-reminders")
    public String enviarRecordatorios(
            @RequestParam(value = "selectedTurnIds", required = false) List<Integer> ids,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (ids == null || ids.isEmpty()) {
            // Envío automático para todos los turnos próximos
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime tomorrow = now.plusDays(1);
            turnService.getUpComingTurnsForReminders(now, tomorrow); // ya incluye el envío
            redirectAttributes.addFlashAttribute("mensaje", "Se enviaron recordatorios a todos los turnos próximos.");
        } else {
            // Envío manual sólo a los seleccionados
            List<TurnDTO> enviados = turnService.sendRemindersTo(ids);
            model.addAttribute("enviados", enviados);
            redirectAttributes.addFlashAttribute("mensaje", "Se enviaron recordatorios a " + enviados.size() + " turno(s) seleccionado(s).");
        }

        System.out.println("Turnos seleccionados: " + ids);
        return "redirect:/web/turns/reminders";
    }
}