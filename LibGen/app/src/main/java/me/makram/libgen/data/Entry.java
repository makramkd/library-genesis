package me.makram.libgen.data;

/**
 * A class that represents an entry on Library Genesis.
 * Created by admin on 9/6/15.
 */
public class Entry {

    /**
     * The id of this entry relative to the search (not absolute id).
     */
    public int id;

    /**
     * Title of the entry.
     */
    public String title;

    /**
     * The author(s) of this entry. May be multiple, but contained in
     * only one string.
     */
    public String author;

    /**
     * Publisher of this entry. May be null/empty.
     */
    public String publisher;

    /**
     * The year this entry was published. Could be -1 (i.e unknown)
     */
    public String year;

    /**
     * Number of pages in this entry. Could be -1 for unknown.
     */
    public String pages;

    /**
     * Language this book was written in. Could be null/empty.
     */
    public String language;

    /**
     * The size of the file in megabytes.
     */
    public String size;

    /**
     * The file extension, e.g pdf, mobi, etc.
     */
    public String extension;

    /**
     * MD5 hash code used to download the book from libgen.io.
     */
    public String md5;

    /**
     * Link to the information page on libgen.
     */
    public String linkToPage;
}
