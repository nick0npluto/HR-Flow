package com.employee.controller;

import com.employee.model.Address;
import com.employee.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return addressService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        if (!addressService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        address.setId(id);
        return ResponseEntity.ok(addressService.save(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        if (!addressService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        addressService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 