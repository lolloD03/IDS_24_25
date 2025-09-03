package com.filiera.adapter;


import com.filiera.model.dto.CarrelloResponseDTO;
import com.filiera.model.dto.ItemCarrelloResponseDTO;
import com.filiera.model.dto.OrdineResponseDTO;
import com.filiera.model.payment.Carrello;
import com.filiera.model.payment.Ordine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarrelloOrdineMapper {

    public CarrelloResponseDTO toCarrelloResponseDTO(Carrello cart) {
        if (cart == null) {
            return null;
        }

        List<ItemCarrelloResponseDTO> itemDTOs = cart.getProducts().stream()
                .map(item -> ItemCarrelloResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .unitPrice(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CarrelloResponseDTO.builder()
                .id(cart.getId())
                .buyerId(cart.getBuyer().getId())
                .items(itemDTOs)
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    public OrdineResponseDTO toOrdineResponseDTO(Ordine order) {
        if (order == null) {
            return null;
        }

        List<ItemCarrelloResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> ItemCarrelloResponseDTO.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .unitPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProductPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrdineResponseDTO.builder()
                .id(order.getNumeroOrdine())
                .buyerId(order.getBuyer().getId())
                .orderDate(order.getDataOrdine())
                .totalAmount(order.getTotalPrice())
                .items(itemDTOs)
                .build();
    }
}