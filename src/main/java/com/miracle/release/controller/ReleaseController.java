package com.miracle.release.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.cognitive.global.bean.Velocity;
import com.miracle.common.bean.FeatureWithEstimates;
import com.miracle.common.response.FeatureResponse;
import com.miracle.release.utility.ReleaseUtility;

@RestController
public class ReleaseController {

	@Autowired
	private ReleaseUtility releaseUtility;

	@PostMapping(value = "/releaseReport")
	@SuppressWarnings("unchecked")
	public ResponseEntity<FeatureResponse> buildFeatures(@RequestBody Map<String, Object> dataMap) {

		FeatureResponse response = new FeatureResponse();
		try {
			List<FeatureWithEstimates> featureWithEstimates = (List<FeatureWithEstimates>) dataMap
					.get("featureEstimates");
			List<Velocity> velocity = (List<Velocity>) dataMap.get("storyStateDetails");

			ObjectMapper objectMapper = new ObjectMapper();
			List<FeatureWithEstimates> featureEstimatesList = objectMapper.convertValue(featureWithEstimates,
					new TypeReference<List<FeatureWithEstimates>>() {
					});
			List<Velocity> velocityList = objectMapper.convertValue(velocity, new TypeReference<List<Velocity>>() {
			});

			response.setObject(releaseUtility.buildEffort(velocityList, featureEstimatesList));
			response.setSuccess(true);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);

		} catch (Exception exception) {
			response.setObject("Failed to calculate total effort from Release service");
			response.setObject(true);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.BAD_GATEWAY);
		}
	}

}
