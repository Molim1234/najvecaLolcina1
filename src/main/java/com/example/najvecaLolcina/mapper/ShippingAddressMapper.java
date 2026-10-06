package com.example.najvecaLolcina.mapper;
import com.example.najvecaLolcina.entity.ShippingAddress;
import com.example.najvecaLolcina.entity.ShippingAddressDTO;
import org.springframework.stereotype.Component;

@Component
public class ShippingAddressMapper {

    public ShippingAddressDTO toDTO(ShippingAddress shippingAddress){
        return new ShippingAddressDTO(shippingAddress.getFirstName(), shippingAddress.getLastName(),
                shippingAddress.getPhoneNumber(), shippingAddress.getEmail(), shippingAddress.getCity(),
                shippingAddress.getNote()
                );
    }

    public ShippingAddress toAdress(ShippingAddressDTO shippingAddressDTO){
        return new ShippingAddress(shippingAddressDTO.getFirstName(), shippingAddressDTO.getLastName(),
                shippingAddressDTO.getPhoneNumber(), shippingAddressDTO.getEmail(), shippingAddressDTO.getCity(),
                shippingAddressDTO.getNote()
                );

    }




}
