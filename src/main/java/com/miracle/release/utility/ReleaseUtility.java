package com.miracle.release.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.miracle.cognitive.global.bean.ReleaseEffort;
import com.miracle.cognitive.global.bean.ReleaseFeature;
import com.miracle.cognitive.global.bean.ReleaseReport;
import com.miracle.cognitive.global.bean.Velocity;
import com.miracle.common.bean.FeatureWithEstimates;

@Service
public class ReleaseUtility {

	public List<ReleaseReport> buildEffort(List<Velocity> velocityList,
			List<FeatureWithEstimates> featureEstimatesList) {
		List<ReleaseReport> reportList = new ArrayList<>();
		for (Velocity velocity : velocityList) {

			List<Integer> temp = new ArrayList<>();
			ReleaseEffort releaseEffort = new ReleaseEffort();

			int storyPoints = velocity.getVelocity();
			int rem = storyPoints;
			List<ReleaseFeature> list = new ArrayList<ReleaseFeature>();

			for (FeatureWithEstimates featureWithEstimate : featureEstimatesList) {
				if (featureWithEstimate.getEffort() <= rem) {
					ReleaseFeature releaseFeature = new ReleaseFeature();
					releaseFeature.setFeatureName(featureWithEstimate.getFeatureName());
					releaseFeature.setFeatureStatus(
							(int) calculatePercentage(featureWithEstimate.getEffort(), featureWithEstimate.getEffort())
									+ "%");
					rem = (int) (rem - featureWithEstimate.getEffort());
					temp.add(featureWithEstimate.getFeatureID());
					list.add(releaseFeature);
				} else {
					ReleaseFeature releaseFeature = new ReleaseFeature();
					releaseFeature.setFeatureName(featureWithEstimate.getFeatureName());
					releaseFeature
							.setFeatureStatus((int) calculatePercentage(rem, featureWithEstimate.getEffort()) + "%");

					featureWithEstimate.setEffort(featureWithEstimate.getEffort() - rem);
					list.add(releaseFeature);
					releaseEffort.setReleaseFeature(list);
					releaseEffort.setTotalEffort(storyPoints);

					ReleaseReport releaseReport = new ReleaseReport();
					releaseReport.setReleaseEffort(releaseEffort);
					releaseReport.setSprintName(velocity.getSprintName());
					reportList.add(releaseReport);

					break;
				}
			}

			for (int tempInt : temp) {
				FeatureWithEstimates featureEstimate = featureEstimatesList.stream()
						.filter(feature -> feature.getFeatureID() == tempInt).findFirst().get();
				featureEstimatesList.remove(featureEstimate);
			}

		}
		return reportList;
	}

	private double calculatePercentage(double obtained, double total) {
		return obtained * 100 / total;
	}

}
