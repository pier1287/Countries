module.exports = async ({github, context, glob}) => {
    const globber = await glob.create('**/*.xml')
    const files = await globber.glob()

    await github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: `⚠️ Ciao ho trovato ${files.length} files .xml`
    })
}