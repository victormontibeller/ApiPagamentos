package com.pagamento.Cartao.Controller;

import com.pagamento.Cartao.DTO.CartaoDTO;
import com.pagamento.Cartao.Model.Cartao;
import com.pagamento.Cartao.Service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/cartao")
@Tag(name = "Gestão de Cartões", description = "Endpoint para manutenção no cadastro de cartões de crédito")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    @Operation(summary = "Efetua a inclusão de um novo Cartão", method = "POST")
    public ResponseEntity<CartaoDTO> cadastrarCartao(@Valid @RequestBody Cartao cartao) {
        var cartaoNovo = cartaoService.criarCartao(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoNovo);
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Obtem os cartões cadastrados efetuando a busca por Cpf", method = "GET")
    public ResponseEntity<List<Cartao>> buscarCartaoPorNumero(@PathVariable String cpf) {
        return ResponseEntity.ok().body(Collections.singletonList((Cartao) cartaoService.buscarCartaoPorNumero(cpf)));
    }

    @GetMapping()
    @Operation(summary = "Obtem todos os Cartoes", method = "GET")
    public ResponseEntity<List<Cartao>> buscarCartao() {
        return ResponseEntity.ok().body(cartaoService.buscarCartao());
    }


}