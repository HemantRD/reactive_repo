package com.example.demo;

import com.vinsys.hrms.idp.reports.vo.DashboardVo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

        DashboardVo.GroupVsIndividualCostSummary groupVsIndividualCostSummary=new DashboardVo.GroupVsIndividualCostSummary();
	}

}
