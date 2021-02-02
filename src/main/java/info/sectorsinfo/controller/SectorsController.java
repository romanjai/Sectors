package info.sectorsinfo.controller;

import info.sectorsinfo.dto.SectorsInputFormDTO;
import info.sectorsinfo.model.Sector;
import info.sectorsinfo.service.SectorService;
import info.sectorsinfo.service.SectorinfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@Controller
public class SectorsController {

  private final SectorService sectorService;

  private final SectorinfoService sectorinfoService;

  @Autowired
  public SectorsController(SectorService sectorService, SectorinfoService sectorinfoService) {
    this.sectorService = sectorService;

    this.sectorinfoService = sectorinfoService;
  }

  @GetMapping("/")
  public String home() {
    return "redirect:/list";
  }

  @GetMapping("sector")
  public String sector(@RequestParam(required = false, name = "id") Long id, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      String username = userPrincipal.getUsername();

      model.addAttribute("sectorinfos", sectorinfoService.list());
      model.addAttribute("dto", (id == null)? SectorsInputFormDTO.empty() : sectorService.getrepoSector(id, username));
    }

    return "sector";
  }

  @PostMapping("save")
  public String save(@Valid @ModelAttribute("dto") SectorsInputFormDTO dto, BindingResult result, Model model) {
    if (result.hasErrors()) {

      model.addAttribute("sectorinfos", sectorinfoService.list());
      model.addAttribute("dto", dto);

      return "sector";
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      String username = userPrincipal.getUsername();

      dto.getSector().setOwner(username);

      if (dto.getSector().getId() == null)
        sectorService.create(dto);
      else
        sectorService.save(dto);
    }

    return "redirect:/list";
  }

  @GetMapping("list")
  public String list(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

      String username = userPrincipal.getUsername();
      List<Sector> list = sectorService.list(username);
      model.addAttribute("sectors", list);
    } else {
      model.addAttribute("sectors", emptyList());
    }

    return "list";
  }
}
