package com.gmall.user.service;

import com.gmall.user.dto.AddressDTO;
import com.gmall.user.entity.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(AddressDTO addressDTO);

    Address updateAddress(AddressDTO addressDTO);

    void deleteAddress(Long id, Long userId);

    Address getAddressById(Long id, Long userId);

    List<Address> listAddresses(Long userId);

    void setDefaultAddress(Long id, Long userId);
}
