package com.gmall.user.controller;

import com.gmall.common.Result;
import com.gmall.user.dto.AddressDTO;
import com.gmall.user.entity.Address;
import com.gmall.user.service.AddressService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @PostMapping("/add")
    public Result<Address> addAddress(@RequestBody @Validated AddressDTO addressDTO) {
        Address address = addressService.addAddress(addressDTO);
        return Result.success(address);
    }

    @PutMapping("/update")
    public Result<Address> updateAddress(@RequestBody @Validated AddressDTO addressDTO) {
        Address address = addressService.updateAddress(addressDTO);
        return Result.success(address);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id, @RequestParam Long userId) {
        addressService.deleteAddress(id, userId);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<Address> getAddressById(@PathVariable Long id, @RequestParam Long userId) {
        Address address = addressService.getAddressById(id, userId);
        if (address == null) {
            return Result.fail("地址不存在");
        }
        return Result.success(address);
    }

    @GetMapping("/list")
    public Result<List<Address>> listAddresses(@RequestParam Long userId) {
        List<Address> addresses = addressService.listAddresses(userId);
        return Result.success(addresses);
    }

    @PostMapping("/default/{id}")
    public Result<Void> setDefaultAddress(@PathVariable Long id, @RequestParam Long userId) {
        addressService.setDefaultAddress(id, userId);
        return Result.success(null);
    }
}
