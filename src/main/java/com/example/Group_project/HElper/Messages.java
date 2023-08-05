package com.example.Group_project.HElper;
// to show message
public class Messages {
        private String content;
        private String type;
// message content and type
    public Messages(String content, String type) {
        this.content = content;
        this.type = type;
    }
// getter and setter
    public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


