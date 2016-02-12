package eu.modernmt.engine.training.preprocessing;

import eu.modernmt.model.ParallelCorpus;
import eu.modernmt.processing.framework.PipelineInputStream;
import eu.modernmt.processing.framework.UnixLineReader;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by davide on 12/02/16.
 */
class PartitionedInputStream implements PipelineInputStream<String> {

    private List<PartitionWriter> partitions;
    private UnixLineReader reader;
    private Locale language;

    private int windowSize;
    private int lineIndex;
    private int partitionIndex;

    public PartitionedInputStream(ParallelCorpus corpus, Locale language, List<PartitionWriter> partitions) throws IOException {
        this.reader = new UnixLineReader(corpus.getContentReader(language));
        this.partitions = partitions;
        this.language = language;

        int lines = corpus.getLineCount();

        int extraLines = 0;
        for (PartitionWriter partition : partitions)
            extraLines += partition.size();

        this.windowSize = extraLines > 0 ? lines / extraLines : Integer.MAX_VALUE;
        this.lineIndex = 0;
        this.partitionIndex = 0;
    }

    private String getLine() throws IOException {
        lineIndex++;
        return reader.readLine();
    }

    private void writeToPartition(String line) throws IOException {
        for (int i = 0; i < partitions.size(); i++) {
            partitionIndex = (partitionIndex + 1) % partitions.size();

            PartitionWriter partition = partitions.get(partitionIndex);
            if (partition.write(line, language))
                break;
        }
    }

    @Override
    public String read() throws IOException {
        String line;

        while ((line = getLine()) != null && (lineIndex % windowSize == 0)) {
            writeToPartition(line);
        }

        return line;
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }

}
