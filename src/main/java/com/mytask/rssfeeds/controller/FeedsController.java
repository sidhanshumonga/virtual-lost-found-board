package com.mytask.rssfeeds.controller;

//import java.awt.List;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mytask.rssfeeds.model.Entries;
import com.mytask.rssfeeds.dao.FeedsDAO;

@Controller

public class FeedsController {
	
	 @Autowired
	private FeedsDAO feedsDao;
	 
	 /** object to get web context loader */
	 WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
	 
	 
	 @RequestMapping(value ="/filter-entry-found",method = RequestMethod.POST)
	 public String savingFoundFeed(@Valid Entries entries,
				BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception {
			boolean filter = feedsDao.isThereAnyMatch(entries, "found");
			if(filter) {
				feedsDao.saveTemp(entries,"found");
				List<Entries> entries_list=feedsDao.getFilterEntries(entries, "found");
				 model.addAttribute("entries_list",entries_list);
				return "redirect:/filter-entries-found";
			}
			else {
				feedsDao.save(entries,"found");
				return "redirect:/dashboard";
				}
		}
	 
	 @RequestMapping(value ="/filter-entry-lost",method = RequestMethod.POST)
	 public String savingLostFeed(@Valid Entries entries,
				BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception {
			boolean filter = feedsDao.isThereAnyMatch(entries, "lost");
			if(filter) {
				feedsDao.saveTemp(entries,"lost");
				 List<Entries> entries_list=feedsDao.getFilterEntries(entries, "lost");
				 model.addAttribute("entries_list",entries_list);
				 return "redirect:/filter-entries-lost";
			}
			else {
				feedsDao.save(entries,"lost");	
				return "redirect:/dashboard";
				}
		}
	 
		@RequestMapping(value ="/filter-entries-lost")
	public ModelAndView filterLostEntriesView(Entries entries, ModelMap model) {
		 List<Entries> entries_list=feedsDao.getFilterEntries(entries, "lost");
		 model.addAttribute("entries_list",entries_list);
	        return new ModelAndView("filterl");   
	}
	
	@RequestMapping(value ="/filter-entries-found")
	public ModelAndView filterFoundEntriesView(Entries entries, ModelMap model) {
		 List<Entries> entries_list=feedsDao.getFilterEntries(entries, "found");
		 model.addAttribute("entries_list",entries_list);
	        return new ModelAndView("filterf");   
	}

		@RequestMapping(value ="/save-lost-entry")
	public String savingLFeed(@Valid Entries entries,
			BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception {
		feedsDao.saveFinal("found");		
		return "redirect:/dashboard";
	}
	
	 	@RequestMapping(value ="/save-found-entry")
		public String savingFFeed(@Valid Entries entries,
				BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception {
			feedsDao.saveFinal("found");		
			return "redirect:/dashboard";
		}
	

	@RequestMapping("/dashboard")  
    public ModelAndView viewentries(ModelMap model){  
        List<Entries> lost_list=feedsDao.getEntries("lost");
        List<Entries> claim_list=feedsDao.getEntries("claim");
        List<Entries> found_list=feedsDao.getEntries("found");
		model.addAttribute("found_list", found_list);
		model.addAttribute("lost_list", lost_list);
		model.addAttribute("claim_list", claim_list);
        return new ModelAndView("dashboard");  
    }
	
	
	@RequestMapping("/lostentry")  
    public ModelAndView lostEntry(ModelMap model){  
        Entries entries = new Entries();
		model.addAttribute("lost-entries", entries);
        return new ModelAndView("lostentry");  
    } 
	
	
	@RequestMapping("/foundentry")  
    public ModelAndView foundEntry(ModelMap model){  
        Entries entries = new Entries();
		model.addAttribute("found-entries", entries);
        return new ModelAndView("foundentry");  
    } 
	
	
	@RequestMapping("/viewallentries/lost")  
    public ModelAndView lostEntries(ModelMap model){  
		List<Entries> entries_list=feedsDao.getAllEntries("lost");
		model.addAttribute("entries_list", entries_list);
        return new ModelAndView("viewallentriesL");  
    } 
	
	@RequestMapping("/viewallentries/claim")  
    public ModelAndView claimEntries(ModelMap model){  
		List<Entries> entries_list=feedsDao.getAllEntries("claim");
		model.addAttribute("entries_list",entries_list);
        return new ModelAndView("viewallentriesC");   
    } 
	
	@RequestMapping("/viewallentries/found")  
    public ModelAndView foundEntries(ModelMap model){  
		List<Entries> entries_list=feedsDao.getAllEntries("found");
		model.addAttribute("entries_list",entries_list);
        return new ModelAndView("viewallentriesF"); 
    } 

	@RequestMapping("/viewentry/lost/{id}")  
    public ModelAndView viewfeedByIdL(ModelMap model,@PathVariable int id){  
        List<Entries> list=feedsDao.getEntryById(id, "lost");
        return new ModelAndView("viewentryByIdL","list",list);  
    }
	@RequestMapping("/viewentry/found/{id}")  
    public ModelAndView viewfeedByIdF(ModelMap model,@PathVariable int id){  
        List<Entries> list=feedsDao.getEntryById(id, "found");
        return new ModelAndView("viewentryByIdF","list",list);  
    } 
	@RequestMapping("/viewentry/claim/{id}")  
    public ModelAndView viewfeedByIdC(ModelMap model,@PathVariable int id){  
        List<Entries> list=feedsDao.getEntryById(id, "claim");
        return new ModelAndView("viewentryByIdC","list",list);  
    } 
	
	@RequestMapping("/claimentrylost/id={id}")  
    public ModelAndView claimlostEntry(ModelMap model, @PathVariable int id){  
        Entries entries = new Entries();
		model.addAttribute("lost-claim-entries", entries);
		model.addAttribute("id", id);
        return new ModelAndView("claimentrylost");  
    } 
	
	
	@RequestMapping("/claimentryfound/id={id}")  
    public ModelAndView claimfoundEntry(ModelMap model,@PathVariable int id){  
        Entries entries = new Entries();
		model.addAttribute("found-claim-entries", entries);
		model.addAttribute("id", id);
        return new ModelAndView("claimentryfound");  
    } 
	
	@RequestMapping(value="/submit-claim-lost/{id}",method = RequestMethod.POST)  
    public String submitLostClaim(Entries entries,@PathVariable int id,
			BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception{  
        feedsDao.saveClaim(entries,id,"lost");
        return "redirect:/dashboard";  
    } 
	
	
	@RequestMapping(value="/submit-claim-found/{id}",method = RequestMethod.POST)  
    public String submitFoundClaim(Entries entries,@PathVariable int id,
			BindingResult result, ModelMap model,RedirectAttributes redirectAttributes) throws Exception{  
		feedsDao.saveClaim(entries,id,"found");
		return "redirect:/dashboard"; 
    } 

}
