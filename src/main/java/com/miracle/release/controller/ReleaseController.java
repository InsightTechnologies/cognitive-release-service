package com.miracle.release.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.common.bean.FeatureWithEstimates;
import com.miracle.common.response.FeatureResponse;

@RestController
public class ReleaseController {

	@PostMapping(value = "/releaseReport")
	public ResponseEntity<FeatureResponse> buildFeatures(@RequestBody Map<String, Object> dataMap) {
//			@RequestBody Map<String, Double> storySprintMap,
//			@RequestBody List<FeatureWithEstimates> storyStates) {

		FeatureResponse response = new FeatureResponse();
		try {
			List<FeatureWithEstimates> featureWithEstimates = (List<FeatureWithEstimates>) dataMap
					.get("featureEstimates");
			Map<String, Double> velocityMap = (Map<String, Double>) dataMap.get("storyStateDetails");
			List<JSONObject> releaseReport = new ArrayList<JSONObject>();
			JSONObject releaseJson = new JSONObject();

			for (String sprint : velocityMap.keySet()) {
				List<Integer> temp = new ArrayList<>();
				JSONObject sprintJson = new JSONObject();
				Double storyPoints = velocityMap.get(sprint);
				double rem = storyPoints;

				for (FeatureWithEstimates featureWithEstimate : featureWithEstimates) {

					if (featureWithEstimate.getEffort() <= rem) {
						sprintJson.put(featureWithEstimate.getFeatureName(),
								calculatePercentage(featureWithEstimate.getEffort(), featureWithEstimate.getEffort()));
						rem = rem - featureWithEstimate.getEffort();
						temp.add(featureWithEstimate.getFeatureID());
					} else {
						sprintJson.put(featureWithEstimate.getFeatureName(),
								calculatePercentage(rem, featureWithEstimate.getEffort()));
						featureWithEstimate.setEffort(featureWithEstimate.getEffort() - rem);
						sprintJson.put("Total", storyPoints);
						break;
					}
				}
				releaseJson.put(sprint, sprintJson);

				for (int tempInt : temp) {
					FeatureWithEstimates featureEstimate = featureWithEstimates.stream()
							.filter(feature -> feature.getFeatureID() == tempInt).findFirst().get();
					featureWithEstimates.remove(featureEstimate);
				}

			}
			// send total effort using FeatureResponse
			response.setObject(releaseJson);
			response.setObject(true);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);
		} catch (Exception exception) {

			response.setObject("Failed to calculate total effort from Release service");
			response.setObject(true);
			return new ResponseEntity<FeatureResponse>(response, HttpStatus.BAD_GATEWAY);
		}
	}

	private double calculatePercentage(double obtained, double total) {
		return obtained * 100 / total;
	}

}
