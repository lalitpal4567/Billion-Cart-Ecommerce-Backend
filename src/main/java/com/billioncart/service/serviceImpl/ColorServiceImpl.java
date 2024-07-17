package com.billioncart.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.model.productCatalogue.Color;
import com.billioncart.payload.ColorRequest;
import com.billioncart.payload.ColorResponse;
import com.billioncart.repository.ColorRepository;
import com.billioncart.service.ColorService;

@Service
public class ColorServiceImpl implements ColorService{
	private ColorRepository colorRepository;
	
	public ColorServiceImpl(ColorRepository colorRepository) {
		this.colorRepository = colorRepository;
	}
	
	@Override
	@Transactional
	public List<ColorResponse> addColors(List<ColorRequest> requests){
		List<Color> colors = requests.stream().map(color ->{
			Color newColor = new Color();
			newColor.setName(color.getName());
			return newColor;
		}).collect(Collectors.toList());
		
		List<Color> createdColors = colorRepository.saveAll(colors);
		
		List<ColorResponse> colorResponses = createdColors.stream().map(color ->{
			return getColorResponse(color);
		}).collect(Collectors.toList());
		
		return colorResponses;
	}
	
	@Override
	@Transactional
	 public Page<ColorResponse> getAllColors(Integer page, Integer size) {
	        Page<Color> colorPage = colorRepository.findAll(PageRequest.of(page, size));
	        
	        Page<ColorResponse> coPage = colorPage.map(color ->{
	        	return getColorResponse(color);
	        });
	        
	        for (ColorResponse colorResponse : coPage) {
				System.out.println(colorResponse.getName());
			}
	        return coPage;
	    }
	
	private ColorResponse getColorResponse(Color color) {
		ColorResponse colorResponse = new ColorResponse();
		colorResponse.setId(color.getColorId());
		colorResponse.setName(color.getName());
		
		return colorResponse;
	}
}
