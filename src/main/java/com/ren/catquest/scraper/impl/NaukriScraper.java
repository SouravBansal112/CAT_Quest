//package com.ren.catquest.scraper.impl;
//
//import com.ren.catquest.job.entity.Company;
//import com.ren.catquest.job.entity.Job;
//import com.ren.catquest.job.entity.JobType;
//import com.ren.catquest.job.entity.Location;
//import com.ren.catquest.scraper.BaseScraper;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class NaukriScraper implements BaseScraper {
//
//    @Override
//    public List<Job> scrapeJobs(String keyword, String location, int maxPages) {
//        List<Job> jobs = new ArrayList<>();
//
//        try {
//            for (int page = 1; page <= maxPages; page++) {
//                String url = String.format(
//                        "https://www.naukri.com/%s-jobs-in-%s-%d",
//                        keyword.replace(" ", "-"),
//                        location.replace(" ", "-"),
//                        page
//                );
//
//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
//                        .timeout(10000)
//                        .get();
//
//                Elements jobCards = doc.select("article.jobTuple");
//
//                for (Element card : jobCards) {
//                    try {
//                        Job job = parseNaukriJob(card);
//                        if (job != null) {
//                            jobs.add(job);
//                        }
//                    } catch (Exception e) {
//                        System.err.println("Error parsing job card: " + e.getMessage());
//                    }
//                }
//
//                // Respectful scraping delay
//                Thread.sleep(2000);
//            }
//        } catch (Exception e) {
//            System.err.println("Error scraping Naukri: " + e.getMessage());
//        }
//
//        return jobs;
//    }
//
//    private Job parseNaukriJob(Element card) {
//        Job job = new Job();
//
//        // Title
//        Element titleEl = card.selectFirst("a.title");
//        if (titleEl != null) {
//            job.setTitle(titleEl.text().trim());
//            job.setJobUrl("https://www.naukri.com" + titleEl.attr("href"));
//        } else {
//            return null;
//        }
//
//        // Company
//        Element companyEl = card.selectFirst("a.subTitle");
//        if (companyEl != null) {
//            Company company = new Company();
//            company.setName(companyEl.text().trim());
//            company.setWebsite("https://www.naukri.com"); // Default
//            job.setCompany(company);
//        }
//
//        // Location
//        Element locationEl = card.selectFirst("li.location");
//        if (locationEl != null) {
//            String[] locParts = locationEl.text().trim().split(",");
//            Location location = new Location();
//            location.setCity(locParts[0].trim());
//            location.setState(locParts.length > 1 ? locParts[1].trim() : "Unknown");
//            location.setCountry("India");
//            job.setLocation(location);
//        }
//
//        // Salary
//        Element salaryEl = card.selectFirst("li.salary");
//        if (salaryEl != null) {
//            job.setSalary(parseSalary(salaryEl.text()));
//        } else {
//            job.setSalary(0);
//        }
//
//        // Type (default to FULL_TIME)
//        job.setType(JobType.FULL_TIME);
//        job.setSource(getSource());
//
//        return job;
//    }
//
//    private int parseSalary(String salaryText) {
//        try {
//            // Extract first number (e.g., "5-7 Lacs PA" -> 500000)
//            String cleaned = salaryText.replaceAll("[^0-9.]", "");
//            if (cleaned.isEmpty()) return 0;
//
//            double salary = Double.parseDouble(cleaned.split("-")[0]);
//
//            if (salaryText.toLowerCase().contains("lac")) {
//                return (int) (salary * 100000);
//            } else if (salaryText.toLowerCase().contains("thousand")) {
//                return (int) (salary * 1000);
//            }
//            return (int) salary;
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    @Override
//    public String getSource() {
//        return "naukri";
//    }
//}
