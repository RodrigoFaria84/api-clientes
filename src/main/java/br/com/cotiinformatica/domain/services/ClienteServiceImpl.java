package br.com.cotiinformatica.domain.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cotiinformatica.domain.dtos.AlterarClienteDto;
import br.com.cotiinformatica.domain.dtos.CriarClienteDto;
import br.com.cotiinformatica.domain.dtos.ObterClienteDto;
import br.com.cotiinformatica.domain.entities.Cliente;
import br.com.cotiinformatica.domain.events.NovoClienteEvent;
import br.com.cotiinformatica.domain.exceptions.CpfJaExistenteException;
import br.com.cotiinformatica.domain.exceptions.EmailJaExistenteException;
import br.com.cotiinformatica.domain.interfaces.ClienteService;
import br.com.cotiinformatica.infrastructure.outbox.OutboxMessage;
import br.com.cotiinformatica.infrastructure.repositories.ClienteRepository;
import br.com.cotiinformatica.infrastructure.repositories.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final OutboxMessageRepository outboxMessageRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	@Override
	public ObterClienteDto criar(CriarClienteDto dto) {
		
		if(clienteRepository.existsByCpf(dto.getCpf())) {
			throw new CpfJaExistenteException(dto.getCpf());
		}			
		
		if(clienteRepository.existsByEmail(dto.getEmail())) {
			throw new EmailJaExistenteException(dto.getEmail());
		}
		
		var mapper = new ModelMapper();
		
		var cliente = mapper.map(dto, Cliente.class);
		
		cliente.setDataHoraCriacao(LocalDateTime.now());
		cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());
		cliente.setAtivo(true);
		
		clienteRepository.save(cliente);
		
		//Criando o evento
		var event = new NovoClienteEvent(
				cliente.getId(),
				cliente.getNome(),
				cliente.getEmail(),
				cliente.getCpf(),
				cliente.getDataHoraCriacao()
				);
		
		//Gravando o evento na tabela de saída (OutboxMessage)
		try {
			
			var outboxMessage = new OutboxMessage();
			outboxMessage.setAggregateType("Cliente"); //nome da entidade
			outboxMessage.setAggregateId(event.id() != null ? event.id().toString() : null); //id do cliente
			outboxMessage.setType("NovoCliente"); //nome do evento
			outboxMessage.setPayload(objectMapper.writeValueAsString(event)); //dados do evento em json
			
			outboxMessageRepository.save(outboxMessage);
		}
		catch(Exception e) {
			throw new IllegalStateException(e.getMessage());
		}		
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public ObterClienteDto alterar(AlterarClienteDto dto) {
		
		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(dto.getId())
						.orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
		
		if(dto.getNome() != null) { 
			cliente.setNome(dto.getNome());
		}
		
		if(dto.getEmail() != null && !dto.getEmail().equals(cliente.getEmail())) {
			if(clienteRepository.existsByEmail(dto.getEmail())) {
				throw new EmailJaExistenteException(dto.getEmail());
			}
			cliente.setEmail(dto.getEmail());
		}
		
		if(dto.getCpf() != null && !dto.getCpf().equals(cliente.getCpf())) {
			if(clienteRepository.existsByCpf(dto.getCpf())) {
				throw new CpfJaExistenteException(dto.getCpf());
			}
			cliente.setCpf(dto.getCpf());
		}
		
		cliente.setDataHoraUltimaAlteracao(LocalDateTime.now());
		
		clienteRepository.save(cliente);
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public ObterClienteDto inativar(UUID id) {

		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
		
		cliente.setAtivo(false);
		
		clienteRepository.save(cliente);
		
		return mapper.map(cliente, ObterClienteDto.class);
	}

	@Override
	public Page<ObterClienteDto> consultarAtivos(int page, int size, String sortBy, String direction) {

		var mapper = new ModelMapper();
		
		var sort = direction.equalsIgnoreCase("desc")
					? Sort.by(sortBy).descending()
					: Sort.by(sortBy).ascending();
		
		var pageable = PageRequest.of(page, size, sort);
		
		return clienteRepository.findByAtivoTrue(pageable)
				.map(cliente -> mapper.map(cliente, ObterClienteDto.class));
	}

	@Override
	public ObterClienteDto obterAtivoPorId(UUID id) {
		
		var mapper = new ModelMapper();
		
		var cliente = clienteRepository.findById(id).get();		
		
		return mapper.map(cliente, ObterClienteDto.class);
	}
}
