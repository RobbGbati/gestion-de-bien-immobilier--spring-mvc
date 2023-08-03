package com.gracetech.gestionimmoback.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gracetech.gestionimmoback.constant.Constants;
import com.gracetech.gestionimmoback.dto.ClientDto;
import com.gracetech.gestionimmoback.dto.GestionImmoResponse;
import com.gracetech.gestionimmoback.exception.CustomFunctionalException;
import com.gracetech.gestionimmoback.service.IClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(Constants.View.CLIENTS)
public class ClientController {
	
	private final IClientService clientService;
	
	@GetMapping("/existByEmail")
	public ResponseEntity<GestionImmoResponse> existByEmail(@RequestParam(name = "email") String email) {
		return ResponseEntity.ok().body(new GestionImmoResponse(clientService.existByEmail(email)));
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<GestionImmoResponse> getAllClients() {
		return ResponseEntity.ok().body(new GestionImmoResponse(clientService.findAll()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GestionImmoResponse> getClientById(@PathVariable Long id) {
		return ResponseEntity.ok().body(new GestionImmoResponse(clientService.getClient(id)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
		clientService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("")
	public ResponseEntity<GestionImmoResponse> createClient(@Valid @RequestBody(required = true) ClientDto client) {
		if (clientService.existByEmail(client.getEmail())) {
			throw new CustomFunctionalException(HttpStatus.BAD_REQUEST, Constants.Errors.EMAIL_TAKEN);
		}
		
		ClientDto created = clientService.save(client);
		
		return ResponseEntity.ok().body(new GestionImmoResponse(created));
	}
	
	@PutMapping("")
	public ResponseEntity<GestionImmoResponse> updateClient(@Valid @RequestBody(required = true) ClientDto client) {
		
		ClientDto dbClient = clientService.getClient(client.getId());
		if (StringUtils.isNotBlank(dbClient.getEmail()) && !dbClient.getEmail().equals(client.getEmail()) 
				&& clientService.existByEmail(client.getEmail())) {
			throw new CustomFunctionalException(HttpStatus.BAD_REQUEST, Constants.Errors.EMAIL_TAKEN);
		}
		
		ClientDto created = clientService.save(client);
		
		return ResponseEntity.ok().body(new GestionImmoResponse(created));
	}

}
