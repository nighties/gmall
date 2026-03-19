package com.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.user.dto.AddressDTO;
import com.gmall.user.entity.Address;
import com.gmall.user.mapper.AddressMapper;
import com.gmall.user.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address addAddress(AddressDTO addressDTO) {
        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault() == 1) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, addressDTO.getUserId());
            wrapper.eq(Address::getIsDefault, 1);
            Address defaultAddress = addressMapper.selectOne(wrapper);
            if (defaultAddress != null) {
                defaultAddress.setIsDefault(0);
                addressMapper.updateById(defaultAddress);
            }
        }

        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setCreateTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());

        addressMapper.insert(address);
        return address;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address updateAddress(AddressDTO addressDTO) {
        Address oldAddress = addressMapper.selectById(addressDTO.getId());
        if (oldAddress == null) {
            throw new RuntimeException("地址不存在");
        }

        if (!oldAddress.getUserId().equals(addressDTO.getUserId())) {
            throw new RuntimeException("无权操作该地址");
        }

        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault() == 1) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, addressDTO.getUserId());
            wrapper.eq(Address::getIsDefault, 1);
            wrapper.ne(Address::getId, addressDTO.getId());
            Address defaultAddress = addressMapper.selectOne(wrapper);
            if (defaultAddress != null) {
                defaultAddress.setIsDefault(0);
                addressMapper.updateById(defaultAddress);
            }
        }

        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUpdateTime(LocalDateTime.now());

        addressMapper.updateById(address);
        return addressMapper.selectById(addressDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long id, Long userId) {
        Address address = addressMapper.selectById(id);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }

        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该地址");
        }

        addressMapper.deleteById(id);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    public List<Address> listAddresses(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.orderByDesc(Address::getIsDefault);
        wrapper.orderByDesc(Address::getUpdateTime);
        return addressMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long id, Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId);
        wrapper.eq(Address::getIsDefault, 1);
        Address defaultAddress = addressMapper.selectOne(wrapper);
        if (defaultAddress != null) {
            defaultAddress.setIsDefault(0);
            addressMapper.updateById(defaultAddress);
        }

        Address address = addressMapper.selectById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在或无权操作");
        }

        address.setIsDefault(1);
        address.setUpdateTime(LocalDateTime.now());
        addressMapper.updateById(address);
    }
}
