package com.huawei.vca.qa;

import com.huawei.vca.grpc.ParagraphFinderController;
import com.huawei.vca.search.SearchController;
import com.huawei.vca.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SimpleQuestionAndAnswerController implements QuestionAndAnswerController {

    @Autowired
    private SearchController searchController;

    @Autowired
    private ParagraphFinderController paragraphFinderController;

    private static final Logger logger = LoggerFactory.getLogger(SimpleQuestionAndAnswerController.class);

    private static final int numOfParagraphs = 10;

    @Override
    public String getAnswer(String question) {

        List<SearchResult> relatedParagraphList = searchController.getRelatedParagraphList(question);
        int size = relatedParagraphList.size();

        if (size == 0) {
            return "Sorry, I have no answer to this question...";
        }

        String[] paragraps = new String[numOfParagraphs];

        for (int i = 0; i < size; i++) {
            paragraps[i] = relatedParagraphList.get(i).getText();
        }

        if (size < numOfParagraphs) {

            int addParagraphs = 10 - size;
            for (int i = 0; i < addParagraphs; i++) {
                paragraps[size + i] = "EMPTY EMPTY EMPTY EMPTY EMPTY";
            }

        }


        String paragraph = paragraphFinderController.getParagraph(question, paragraps);
        logger.debug("**** " + paragraph + " *****");
        return paragraph;
    }
}
