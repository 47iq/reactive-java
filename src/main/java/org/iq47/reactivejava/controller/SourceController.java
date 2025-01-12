package org.iq47.reactivejava.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.iq47.reactivejava.service.FlowableService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SourceController {
    private final FlowableService flowableService;
    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public String getExampleHTML(Model model) {
        model.addAttribute("avgValue", flowableService.getCurrentMetrics());
        model.addAttribute("inputSpeed", flowableService.getInputSpeed());
        model.addAttribute("threadsCount", flowableService.getThreadsCount());
        return "index.html";
    }

    @GetMapping("/data")
    public ResponseEntity<MetricsResponse> getData() {
        MetricsResponse response = new MetricsResponse();
        response.setAvgValue(flowableService.getCurrentMetrics());
        response.setInputSpeed(flowableService.getInputSpeed());
        response.setThreadsCount(flowableService.getThreadsCount());
        response.setHandleDelay(flowableService.getBackpressureSubscriber().getDelay());
        response.setQueueSize(Integer.MAX_VALUE - flowableService.getPool().getQueue().remainingCapacity());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/data")
    public ResponseEntity<Void> postData(@RequestBody MetricsRequest request) {
        flowableService.updateConfig(request.getInputSpeed());
        return ResponseEntity.ok().build();
    }

    @Data
    static class MetricsRequest {
        private int inputSpeed;
    }

    @Data
    static class MetricsResponse {
        private Map<String, Double> avgValue;
        private int inputSpeed;
        private int threadsCount;
        int queueSize;
        int handleDelay;
    }

}
