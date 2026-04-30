package com.example.news.api.dto.analytics;

import java.util.ArrayList;
import java.util.List;

public class GraphResponseDTO {
    private List<GraphNodeDTO> nodes = new ArrayList<>();
    private List<GraphLinkDTO> links = new ArrayList<>();

    public GraphResponseDTO() {
    }

    public GraphResponseDTO(List<GraphNodeDTO> nodes, List<GraphLinkDTO> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public List<GraphNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<GraphLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<GraphLinkDTO> links) {
        this.links = links;
    }
}
