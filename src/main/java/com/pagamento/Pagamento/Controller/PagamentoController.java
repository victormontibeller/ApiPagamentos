package com.pagamento.Pagamento.Controller;

import com.pagamento.Pagamento.DTO.PagamentoPorClienteDto;
import com.pagamento.Exception.LimiteCartaoException;
import com.pagamento.Pagamento.Model.Pagamento;
import com.pagamento.Pagamento.Service.PagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Gestão de Pagamentos", description = "Controller para manutenção dos  pagamentos da nossa aplicação")
public class PagamentoController {

    @Autowired
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(summary = "Efetua a inclusão de um novo Pagamento", method = "POST")
    public ResponseEntity<?> cadastrarPagamento(@Valid @RequestBody Pagamento pagamento) {
        try {
            var pagamentoNovo = pagamentoService.cadastrarPagamento(pagamento);
            return ResponseEntity.status(HttpStatus.OK).body(pagamentoNovo);
        } catch (LimiteCartaoException ex) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(ex);
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
        }
    }

    @GetMapping("/cliente/{cpf}")
    @Operation(summary = "Lista Pagamentos de um determinado cliente", method = "GET")
    public ResponseEntity<?> listaPagamentosPorCliente(@PathVariable String cpf) {
        try {
            List<PagamentoPorClienteDto> pagamentos = pagamentoService.listaPagamentosPorCliente(cpf);
            return ResponseEntity.status(HttpStatus.OK).body(pagamentos);
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}