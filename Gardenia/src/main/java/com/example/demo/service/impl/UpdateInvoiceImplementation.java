package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.UpdateInvoiceStatus;

import com.example.demo.service.UpdateInvoiceStatusService;

public class UpdateInvoiceImplementation implements UpdateInvoiceStatusService {

	@Autowired
	private UpdateInvoiceStatus updateInvoiceStatus;
	
	@Override
	public UpdateInvoiceStatus saveupInvoiceStatus(UpdateInvoiceStatus updateInvoiceStatus )
{
		return updateInvoiceStatus.save(updateInvoiceStatus);
	}
}
