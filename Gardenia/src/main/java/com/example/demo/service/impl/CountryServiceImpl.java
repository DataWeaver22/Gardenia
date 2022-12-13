package com.example.demo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;
import com.example.demo.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService{
	
	private CountryRepository countryRepository;

	public CountryServiceImpl(CountryRepository countryRepository) {
		super();
		this.countryRepository = countryRepository;
	}
	
	@Override
	public List<Country> getAllCountry() {
		return countryRepository.findAll();
	}

	@Override
	public Country saveCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public Country getCountryById(Long id) {
		return countryRepository.findById(id).get();
	}

	@Override
	public Country editCountry(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public void deleteCountryById(Long id) {
		// TODO Auto-generated method stub
		countryRepository.deleteById(id);
	}

//	@Override
//    public Country createCountry(Country country) {
//
//        // convert DTO to entity
//		Country post = mapToEntity(country);
//		Country newPost = countryRepository.save(post);
//
//        // convert entity to DTO
//		Country countryResponse = mapToDTO(newPost);
//        return countryResponse;
//    }
//	
//	@Override
//    public Country getAllCountries(int pageNo, int pageSize, String sortBy, String sortDir) {
//
//        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        // create Pageable instance
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//        Page<Country> posts = countryRepository.findAll(pageable);
//
//        // get content for page object
//        List<Country> listOfPosts = posts.getContent();
//
//        List<PostDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
//
//        PostResponse postResponse = new PostResponse();
//        postResponse.setContent(content);
//        postResponse.setPageNo(posts.getNumber());
//        postResponse.setPageSize(posts.getSize());
//        postResponse.setTotalElements(posts.getTotalElements());
//        postResponse.setTotalPages(posts.getTotalPages());
//        postResponse.setLast(posts.isLast());
//
//        return postResponse;
//    }
//	
//	// convert Entity into DTO
//    private Country mapToDTO(Country post){
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
//        return postDto;
//    }
//
//    // convert DTO to entity
//    private Country mapToEntity(PostDto postDto){
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
//        return post;
//    }
//	
}
