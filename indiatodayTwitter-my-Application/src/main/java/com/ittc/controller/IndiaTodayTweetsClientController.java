package com.ittc.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ittc.javabeans.NewsJavaBean;
import com.ittc.utility.DateFinderUtility;
import com.ittc.utility.TweetToNewsJavaBeanConverter;

/**
 * @author Dipanjay
 */

@Controller
@RequestMapping
public class IndiaTodayTweetsClientController {

	public static final String BASE_URI = "/ind2daytweets";

	@Autowired
	private DateFinderUtility finder; 			//Uility class for identifying the date and time 
	
	/*	Twitter interface provided by Spring Social to interact with Twitter end point by providing Oauth tokenId and secret key */
	@Autowired
	private Twitter twitter;

	@Autowired
	private TweetToNewsJavaBeanConverter converter;

	@GetMapping(value = BASE_URI)
	public String getTweetsOfIndiaToday(ModelMap modelMap) {

				fetchTweetsOfIndiaToday(modelMap);

		return "newsFeed";
	}

	/* using this method simplifying the logic in controller getTweetsOfIndiaToday() */
	public void fetchTweetsOfIndiaToday(ModelMap modelMap) {
		
		modelMap.clear();
		Set<NewsJavaBean> newsBeans = null;
		List<Tweet> tweets = null;

		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy h:mm a Z");
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		Date d = new Date();
		sdf.setTimeZone(istTimeZone);
		String strtime = sdf.format(d);
		
		tweets = twitter.timelineOperations().getUserTimeline("@IndiaToday", 100);
		newsBeans = converter.getAllNewsJavaBeans(tweets);

		modelMap.addAttribute("newsBeans", newsBeans);
		modelMap.addAttribute("date", finder.getFormatTimeDt(strtime));

	}
}
