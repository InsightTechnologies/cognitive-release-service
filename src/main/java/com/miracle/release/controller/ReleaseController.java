package com.miracle.release.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.cognitive.response.FeatureResponse;
import com.miracle.scrum.bean.FeatureWithEstimates;

@RestController
public class ReleaseController {

	@GetMapping(value = "/effort")
	public ResponseEntity<FeatureResponse> buildFeatures(
			@RequestParam(value = "storySprintMap") Map<String, Double> storySprintMap,
			@RequestParam(value = "storyStates") List<FeatureWithEstimates> storyStates) {

		for (Entry<String, Double> entrySet : storySprintMap.entrySet()) {
			for
		}

		FeatureResponse response = new FeatureResponse();
		// send total effort using FeatureResponse

		return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);
	}

}
