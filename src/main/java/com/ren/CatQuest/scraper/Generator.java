//package com.ren.CatQuest.scraper;
//
//import com.ren.CatQuest.model.*;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class Generator {
//
//    private static final String[] TITLES = {
//            "Software Engineer", "Backend Developer", "Frontend Developer",
//            "Data Analyst", "DevOps Engineer", "Fullstack Developer"
//    };
//
//    private static final String[] COMPANIES = {
//            "TechCorp", "InnoSoft", "CodeWorks", "DataHive", "CloudNine"
//    };
//
//    private static final String[] WEBSITES = {
//            "techcorp.com", "innosoft.com", "codeworks.com", "datahive.com", "cloudnine.com"
//    };
//
//    private static final String[] CITIES = {"New York", "San Francisco", "Chicago", "Austin", "Seattle"};
//    private static final String[] STATES = {"NY", "CA", "IL", "TX", "WA"};
//    private static final String[] COUNTRIES = {"USA"};
//
//    private static final JobType[] TYPES = JobType.values();
//
//    private final Random random = new Random();
//
//    public List<Job> generateJobs(int count) {
//        List<Job> jobs = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            // Generate company
//            int companyIndex = random.nextInt(COMPANIES.length);
//            Company company = new Company();
//            company.setName(COMPANIES[companyIndex]);
//            company.setWebsite(WEBSITES[companyIndex]);
//
//            // Generate location
//            int locIndex = random.nextInt(CITIES.length);
//            Location location = new Location();
//            location.setCity(CITIES[locIndex]);
//            location.setState(STATES[locIndex]);
//            location.setCountry(COUNTRIES[0]);
//
//            // Generate job
//            Job job = new Job();
//            job.setTitle(TITLES[random.nextInt(TITLES.length)]);
//            job.setCompany(company);
//            job.setLocation(location);
//            job.setSalary(50000 + random.nextInt(100000)); // 50k–150k
//            job.setType(TYPES[random.nextInt(TYPES.length)]);
//            job.setJobUrl("https://example.com/jobs/" + i);
//            job.setSource("generator");
//
//            jobs.add(job);
//        }
//
//        return jobs;
//    }
//}

package com.ren.CatQuest.scraper;

import com.ren.CatQuest.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Generator {

    private static final String[] TITLES = {
            "Software Engineer", "Backend Developer", "Frontend Developer",
            "Data Analyst", "DevOps Engineer", "Fullstack Developer"
    };

    private static final String[] COMPANIES = {
            // Existing
            "TechCorp", "InnoSoft", "CodeWorks", "DataHive", "CloudNine",
            "ByteBridge", "NextGen Labs", "VisionSoft", "AlgoForge", "Zenith Systems",
            "QuantumWare", "PixelPulse", "NovaTech", "Aether Dynamics", "BlueOrbit",
            // Added (Global)
            "GloboTech", "PrimeEdge", "AstraNova", "SkyLink", "MetaLogic",
            "Orion Labs", "SoftNexus", "LumaCore", "CypherPeak", "RedStack",
            "NeuraLinker", "HyperBase", "Vortex Dynamics", "OmniNet", "DeltaSoft",
            // Added (India + others)
            "Infosys", "TCS", "Wipro", "Zoho", "Tech Mahindra",
            "Mindtree", "Freshworks", "HCL", "Byteline", "Innovacer",
            "Grab", "Gojek", "Naver", "Rakuten", "Samsung Research",
            "ByteDance", "Huawei", "Alibaba Cloud", "Tencent Labs", "Baidu Tech"
    };

    private static final String[] WEBSITES = {
            // Existing
            "techcorp.com", "innosoft.com", "codeworks.com", "datahive.com", "cloudnine.com",
            "bytebridge.io", "nextgenlabs.com", "visionsoft.dev", "algoforge.tech", "zenithsystems.io",
            "quantumware.ai", "pixelpulse.co", "novatech.net", "aetherdynamics.com", "blueorbit.io",
            // Added (Global)
            "globotech.com", "primeedge.io", "astranova.net", "skylink.dev", "metalogic.com",
            "orionlabs.ai", "softnexus.co", "lumacore.io", "cypherpeak.tech", "redstack.dev",
            "neuralinker.com", "hyperbase.io", "vortexdynamics.net", "omninetworks.com", "deltasoft.ai",
            // Added (India + others)
            "infosys.com", "tcs.com", "wipro.com", "zoho.com", "techmahindra.com",
            "mindtree.com", "freshworks.com", "hcltech.com", "byteline.io", "innovacer.com",
            "grab.com", "gojek.com", "naver.com", "rakuten.com", "samsung.com",
            "bytedance.com", "huawei.com", "alibabacloud.com", "tencent.com", "baidu.com"
    };

    private static final String[] CITIES = {
            // USA
            "New York", "San Francisco", "Chicago", "Austin", "Seattle",
            // India
            "Bangalore", "Hyderabad", "Pune", "Chennai", "Gurgaon", "Noida", "Mumbai",
            // Other countries
            "Tokyo", "Osaka", "Seoul", "Busan", "Singapore", "Jakarta",
            "Beijing", "Shanghai", "Shenzhen", "Guangzhou",
            "London", "Manchester", "Berlin", "Munich", "Paris", "Lyon",
            "Toronto", "Vancouver", "Sydney", "Melbourne", "Dubai"
    };

    private static final String[] STATES = {
            // USA
            "NY", "CA", "IL", "TX", "WA",
            // India
            "KA", "TG", "MH", "TN", "HR", "UP", "DL",
            // Other
            "Tokyo", "Osaka", "Seoul", "Busan", "SG", "Jakarta", "Beijing", "Shanghai", "Berlin", "Ontario",
            "New South Wales", "Victoria", "Dubai"
    };

    private static final String[] COUNTRIES = {
            "USA", "India", "Japan", "South Korea", "Singapore",
            "Indonesia", "China", "Germany", "France", "UK", "Canada", "Australia", "UAE", "Brazil", "Spain"
    };

    private static final JobType[] TYPES = JobType.values();

    private final Random random = new Random();

    public List<Job> generateJobs(int count) {
        List<Job> jobs = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            // Generate company
            int companyIndex = random.nextInt(COMPANIES.length);
            Company company = new Company();
            company.setName(COMPANIES[companyIndex]);
            company.setWebsite(WEBSITES[companyIndex]);

            // Generate location
            int locIndex = random.nextInt(CITIES.length);
            Location location = new Location();
            location.setCity(CITIES[locIndex]);
            location.setState(STATES[random.nextInt(STATES.length)]);
            location.setCountry(COUNTRIES[random.nextInt(COUNTRIES.length)]);

            // Generate job
            Job job = new Job();
            job.setTitle(TITLES[random.nextInt(TITLES.length)]);
            job.setCompany(company);
            job.setLocation(location);
            job.setSalary(50000 + random.nextInt(100000)); // 50k–150k
            job.setType(TYPES[random.nextInt(TYPES.length)]);
            job.setJobUrl("https://example.com/jobs/" + i);
            job.setSource("generator");

            jobs.add(job);
        }

        return jobs;
    }
}
