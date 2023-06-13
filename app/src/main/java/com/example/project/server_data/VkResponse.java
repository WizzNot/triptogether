package com.example.project.server_data;

import java.util.ArrayList;


public class VkResponse{ //POJO для десериализация данных ответа VkApi
    public ArrayList<Response_users_get> response;

    public ArrayList<Response_users_get> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Response_users_get> response) {
        this.response = response;
    }
    public class Response_users_get{
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<Object> getOauth_verification() {
            return oauth_verification;
        }

        public void setOauth_verification(ArrayList<Object> oauth_verification) {
            this.oauth_verification = oauth_verification;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public boolean isCan_access_closed() {
            return can_access_closed;
        }

        public void setCan_access_closed(boolean can_access_closed) {
            this.can_access_closed = can_access_closed;
        }

        public boolean isIs_closed() {
            return is_closed;
        }

        public void setIs_closed(boolean is_closed) {
            this.is_closed = is_closed;
        }

        public int id;
        public ArrayList<Object> oauth_verification;
        public String first_name;
        public String last_name;
        public boolean can_access_closed;
        public boolean is_closed;

        @Override
        public String toString() {
            return "Response{" +
                    "id=" + id +
                    ", oauth_verification=" + oauth_verification +
                    ", first_name='" + first_name + '\'' +
                    ", last_name='" + last_name + '\'' +
                    ", can_access_closed=" + can_access_closed +
                    ", is_closed=" + is_closed +
                    '}';
        }
    }
}



