package com.hackerrank.movielist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MovieListFromJson {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List> getMovieList(String str) throws IOException {
		Map<String, List> map = new HashMap<>();
		List<String> movieList = new ArrayList<String>();
		JsonObject obj;
		for (int i = 1; i <= getTotalPage(str); i++) {
			JsonArray data = getJsonData(str, i);
			for (int j = 0; j < data.size(); j++) {
				obj = data.get(j).getAsJsonObject();
				String year = obj.get("Year").getAsString();
				String title = obj.get("Title").getAsString();
				if (map.get(year) != null) {
					movieList = map.get(year);
					movieList.add(title);
					map.put(year, movieList);
				} else {
					movieList = new ArrayList<String>();
					movieList.add(title);
					map.put(year, movieList);
				}
			}
		}
		return map;
	}



	public JsonArray getJsonData(String val, int page) throws JsonSyntaxException, IOException {
		String url = "https://jsonmock.hackerrank.com/api/movies/search/?Title=" + val + "&page=" + page;
		JsonArray data = null;
		try {
			URL uri = new URL(url);
			HttpURLConnection con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("accept", "application/json");
			if (con.getResponseCode() == 200) {
				BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = response.readLine()) != null) {
					sb.append(output);
				}
				//System.out.println(sb.toString());
				JsonParser parsor = new JsonParser();
				JsonObject obj = (JsonObject) parsor.parse(sb.toString());
				data = obj.getAsJsonArray("data");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return data;

	}

	public int getTotalPage(String val) throws JsonSyntaxException, IOException {
		String url = "https://jsonmock.hackerrank.com/api/movies/search/?Title=" + val;
		int totalPage = 0;
		try {
			URL uri = new URL(url);
			HttpURLConnection con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("accept", "application/json");
			if (con.getResponseCode() == 200) {
				BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = response.readLine()) != null) {
					sb.append(output);
				}
				//System.out.println(sb.toString());
				JsonParser parsor = new JsonParser();
				JsonObject obj = (JsonObject) parsor.parse(sb.toString());

				totalPage = obj.get("total_pages").getAsInt();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return totalPage;
	}

	public static void main(String[] args) {
		try {
			System.out.println(new MovieListFromJson().getMovieList("Waterworld"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
