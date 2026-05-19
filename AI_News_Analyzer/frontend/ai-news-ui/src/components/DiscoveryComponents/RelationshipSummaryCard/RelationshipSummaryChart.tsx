import { useState, useEffect, useCallback, useRef } from 'react';

import ForceGraph2D, { type ForceGraphMethods } from 'react-force-graph-2d';
import { useElementSize } from '@mantine/hooks';
import { fetchRelationshipGraphData } from '../../../services/analysisService';
import type { RelationshipGraph } from '../../../shared/interfaces/RelationshipGraph';
import { getColorCode } from '../../../shared/utils/getColorCode';
import { useEntityRelationship } from '../../../shared/custom_hooks/useEntityRelationship.ts';
import { SentimentColors } from '@/shared/constants/Colors';

const RelationshipSummaryChart = () => {
  const { entityRelationshipInterval } = useEntityRelationship();
  const { ref, width, height } = useElementSize();
  const [relationshipGraph, setRelationshipGraph] =
    useState<RelationshipGraph>();

  const fgRef = useRef<ForceGraphMethods>();

  const handleEngineStop = () => {
    // 2. Use the ref to call zoomToFit
    if (fgRef.current) {
      fgRef.current.zoomToFit(400, 50); // 400ms transition, 50px padding
    }
  };

  const fetchRelationshipGraph = useCallback(
    async (intervalUnit: string, amount: number) => {
      try {
        const result = await fetchRelationshipGraphData(intervalUnit, amount);
        setRelationshipGraph(result);
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    },
    [],
  );

  useEffect(() => {
    const loadRelationshipGraphData = async () => {
      await fetchRelationshipGraph(
        entityRelationshipInterval.intervalUnit,
        entityRelationshipInterval.amount,
      );
    };
    loadRelationshipGraphData();
  }, [fetchRelationshipGraph, entityRelationshipInterval]);

  return (
    <div
      ref={ref}
      style={{
        width: '100%',
        height: 'calc(100vh - 200px)', // Adjust 220px based on Taskbar height
        position: 'relative',
        overflow: 'hidden',
      }}
    >
      {width > 0 && (
        <ForceGraph2D
          width={width}
          height={height}
          graphData={relationshipGraph}
          nodeLabel='id'
          nodeAutoColorBy='group'
          linkWidth={(link) => link.value}
          linkColor={(link) =>
            link.sentiment > 0
              ? getColorCode(SentimentColors.postive)
              : link.sentiment < 0
                ? getColorCode(SentimentColors.negative)
                : getColorCode(SentimentColors.neutral)
          }
          nodeVal={(node) => Math.log(node.size + 1) * 3}
          linkDirectionalParticles={2}
          // This keeps the graph centered in your dynamic div
          onEngineStop={handleEngineStop}
          nodeCanvasObject={(node, ctx, globalScale) => {
            const label = node.id;
            const fontSize = 14 / globalScale;
            const radius = Math.max(Math.log(node.size + 1) * 3, 3);

            let nodeColor = getColorCode(SentimentColors.neutral); // Default Neutral
            if (node.sentiment > 0)
              nodeColor = getColorCode(SentimentColors.postive); // Positive
            if (node.sentiment < 0)
              nodeColor = getColorCode(SentimentColors.negative); // Negative

            // Draw Node Circle
            ctx.beginPath();
            ctx.arc(node.x, node.y, radius, 0, 2 * Math.PI, false);
            ctx.fillStyle = nodeColor;
            ctx.fill();

            // Draw Text
            ctx.font = `${fontSize}px Inter, Sans-Serif`;
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillStyle = '#000';
            ctx.fillText(label, node.x, node.y + radius + fontSize);
          }}
          linkCanvasObject={(link, ctx, globalScale) => {
            // 1. Zoom Threshold: Only draw text if zoomed in enough (e.g., scale > 0.8)
            // This prevents the "text storm" when looking at the whole graph.
            if (globalScale < 0.8) return;

            const start = link.source;
            const end = link.target;
            if (typeof start !== 'object' || typeof end !== 'object') return;

            // 2. Fixed Font Size: Removing the "/ globalScale" makes it grow with the zoom
            const fontSize = 2;

            const textPos = {
              x: start.x + (end.x - start.x) * 0.5,
              y: start.y + (end.y - start.y) * 0.5,
            };

            const label = `W: ${link.value} | S: ${link.sentiment > 0 ? '+' : ''}${link.sentiment.toFixed(2)}`;

            // 3. Draw Background (White pill shape)
            const textWidth = ctx.measureText(label).width;
            const padding = 1;
            const bckgDimensions = [textWidth + padding, fontSize + padding];

            ctx.fillStyle = 'rgba(255, 255, 255, 0.9)';
            ctx.fillRect(
              textPos.x - bckgDimensions[0] / 2,
              textPos.y - bckgDimensions[1] / 2,
              bckgDimensions[0],
              bckgDimensions[1],
            );

            // 4. Draw Text
            ctx.font = `${fontSize}px Inter, Sans-Serif`;
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillStyle =
              link.sentiment < 0
                ? getColorCode(SentimentColors.negative)
                : link.sentiment > 0
                  ? getColorCode(SentimentColors.postive)
                  : getColorCode(SentimentColors.neutral);
            ctx.fillText(label, textPos.x, textPos.y);
          }}
          // IMPORTANT: This prevents the text from flickering
          linkCanvasObjectMode={() => 'after'}
        />
      )}
    </div>
  );
};

export default RelationshipSummaryChart;
