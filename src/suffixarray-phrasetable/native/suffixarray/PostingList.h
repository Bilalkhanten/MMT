//
// Created by Davide  Caroselli on 30/09/16.
//

#ifndef SAPT_POSTINGLIST_H
#define SAPT_POSTINGLIST_H

#include <string>
#include <mmt/sentence.h>
#include <unordered_set>
#include <map>

using namespace std;

namespace mmt {
    namespace sapt {

        class PostingList {
        public:

            PostingList() {};

            void Append(domain_t domain, const char *data, size_t size, const unordered_set<int64_t> *filterBy = NULL);

            void Append(domain_t domain, int64_t location, length_t offset);

            void Append(const PostingList &other);

            unordered_set<int64_t> GetLocations() const;

            void Retain(const PostingList &successors, length_t start);

            map<int64_t, pair<domain_t, vector<length_t>>>
            GetSamples(size_t limit = 0, unsigned int shuffleSeed = 0) const;

            string Serialize() const;

            size_t size() const;

            bool empty() const;

        private:
            vector<pair<domain_t, size_t>> domains;
            vector<char> data;

            map<int64_t, pair<domain_t, vector<length_t>>> CollectAll(size_t size_limit) const;
        };

    }
}


#endif //SAPT_POSTINGLIST_H
