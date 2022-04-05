package com.salat23.bot.botapi.message_tools;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HardcodedResources implements IResponseResourceProvider {

    @Override
    public List<MessageResource> getMessageResources() {
        List<MessageResource> resources = new ArrayList<>();

        return resources;
    }

}
